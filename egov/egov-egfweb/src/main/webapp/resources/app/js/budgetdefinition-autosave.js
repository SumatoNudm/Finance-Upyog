(function ($, window) {
    'use strict';

    var DRAFT_VERSION = 1;
    var DRAFT_TTL_MS = 24 * 60 * 60 * 1000;   // draft is removed 24 hours after it was first saved
    var DEBOUNCE_MS = 500;
    var SAFETY_SAVE_MS = 30000;               // periodic save; it also removes the draft once 24h have passed
    var CHAIN_POLL_MS = 400;
    var LISTS_READY_POLLS = 8;
    var GIVE_UP_POLLS = 16;
    var CHAIN_MAX_POLLS = 30;

    // Field names never written to storage. Nothing personal on this form.
    var secretFieldRegex = /password|csrf|token/i;
    // Input types that carry no user data.
    var skipTypeRegex = /^(button|submit|reset|file|image|password|hidden)$/i;
    // Financial Year drives the lists of the other two selects.
    var ROOT_SELECT = 'financialYear';
    var DEPENDENT_SELECTS = ['parent', 'referenceBudget'];

    /* ---------- storage helpers (never throw) ---------- */

    var storage = (function () {
        try {
            var t = '__draft_test__';
            window.sessionStorage.setItem(t, t);
            window.sessionStorage.removeItem(t);
            return window.sessionStorage;
        } catch (e) {
            return null; // blocked or unavailable
        }
    })();

    function removeDraft(key) {
        if (!storage) return;
        try { storage.removeItem(key); } catch (e) {}
    }

    /** Remove this draft if 24 hours have passed since it was first saved. Returns true if removed. */
    function expireIfStale(key) {
        if (!storage) return false;
        try {
            var raw = storage.getItem(key);
            if (!raw) return false;
            var env = JSON.parse(raw);
            if (env && env.createdAt && (Date.now() - env.createdAt) <= DRAFT_TTL_MS) return false;
        } catch (e) {}
        removeDraft(key);   // expired, corrupt or old format
        return true;
    }

    function readDraft(key) {
        if (!storage || expireIfStale(key)) return null;
        try {
            var raw = storage.getItem(key);
            var env = raw && JSON.parse(raw);
            return env && env.v === DRAFT_VERSION && env.data ? env.data : null;
        } catch (e) {
            removeDraft(key);
            return null;
        }
    }

    function writeDraft(key, data) {
        if (!storage) return;
        try {
            var existing = storage.getItem(key);
            var old = existing && JSON.parse(existing);
            storage.setItem(key, JSON.stringify({
                v: DRAFT_VERSION,
                createdAt: (old && old.createdAt) || Date.now(),   // first-save time: the 24h window never slides
                data: data
            }));
        } catch (e) {
            if (window.console) console.warn('Draft not saved (storage full or blocked):', e && e.name);
        }
    }

    /** Dispatches a real change event, so inline onchange, jQuery and addEventListener handlers run. */
    function fireChange(el) {
        if (!el || !el[0]) return;
        var ev;
        try {
            ev = new Event('change', { bubbles: true });
        } catch (e) {
            ev = document.createEvent('Event');
            ev.initEvent('change', true, true);
        }
        el[0].dispatchEvent(ev);
    }

    /** Fields that are never stored: unnamed inputs and secrets. */
    function isSkippedName(name) {
        return !name || secretFieldRegex.test(name);
    }

    /* ---------- main ---------- */

    $(function () {
        var formObj = $('#financialYear, #referenceBudget, #name').first().closest('form');
        if (formObj.length === 0 || !storage) return;

        // Existing budget (edit/modify/view): the server data is the truth, never a local draft.
        var budgetId = $.trim($('#budgetId').val() || '');
        var mode = $.trim($('#mode').val() || '').toLowerCase();
        var modify = $.trim($('#modify').val() || '');
        if (budgetId || mode === 'edit' || modify) return;

        var userId = String(formObj.attr('data-draft-user') || 'anonymous');
        var prefix = 'budget_definition_draft_';
        var storageKey = prefix + userId + '_' + window.location.pathname;

        var state = {
            closed: false,      // true after submit/discard/expiry: no more saving
            enabled: false,     // true after the restore has finished
            dirty: false,       // true once the user (or a restore) has changed something
            restored: false,    // true when a saved draft was applied
            baseline: null,     // snapshot of the untouched form
            debounce: null,
            interval: null
        };

        function byName(name) {
            return $('[name="' + name.replace(/"/g, '\\"') + '"]', formObj);
        }

        function collect() {
            var data = {};
            var checkboxes = {};
            formObj.find(':input').each(function () {
                var el = $(this);
                var name = el.attr('name');
                var type = (el.attr('type') || '').toLowerCase();
                if (isSkippedName(name) || skipTypeRegex.test(type)) return;
                if (el.prop('disabled')) return;               // locked or derived, never submitted
                if (type === 'checkbox') {
                    // Both states are saved, so an unticked box can be restored.
                    checkboxes[name + '|' + el.val()] = el.is(':checked') ? 1 : 0;
                    return;
                }
                if (type === 'radio' && !el.is(':checked')) return;
                var value = el.val();
                if (typeof value !== 'string') return;
                data[name] = value;
            });
            data.__checkboxes = checkboxes;
            return data;
        }

        // Snapshot of the untouched form, taken BEFORE any restore, so a restored draft
        // is never mistaken for "nothing changed" and deleted.
        state.baseline = JSON.stringify(collect());

        function save() {
            if (state.closed || !state.enabled) return;
            // Draft already past its 24h: remove it and stop saving, so an unload/reload
            // save cannot recreate it with a fresh timestamp.
            if (expireIfStale(storageKey)) { stopSaving(); return; }
            var json = JSON.stringify(collect());
            if (!state.dirty && !state.restored) { state.baseline = json; return; }
            if (json === state.baseline) { removeDraft(storageKey); return; } // nothing changed
            writeDraft(storageKey, collect());
        }

        function scheduleSave() {
            clearTimeout(state.debounce);
            state.debounce = setTimeout(save, DEBOUNCE_MS);
        }

        function stopSaving() {
            state.closed = true;
            clearInterval(state.interval);
            clearTimeout(state.debounce);
        }

        function closeDraft() {
            stopSaving();
            removeDraft(storageKey);
        }

        function enableSaving() {
            state.enabled = true;
            if (readDraft(storageKey)) save();
        }

        /* ---- restore ---- */

        function optionExists(el, value) {
            return el.find('option').filter(function () { return $(this).val() === value; }).length > 0;
        }

        function optionSignature(el) {
            return $.map(el.find('option'), function (o) { return o.value; }).join('\u0001');
        }

        function restore(data) {
            // 1. checkboxes (Active, Primary), including the unticked state
            $.each(data.__checkboxes || {}, function (key, on) {
                var sep = key.indexOf('|');
                var name = key.substring(0, sep);
                var val = key.substring(sep + 1);
                var cb = byName(name).filter(':checkbox').filter(function () {
                    return $(this).val() === val;
                }).first();
                if (cb.length && !cb.prop('disabled') && cb.is(':checked') !== !!on) cb[0].click();
            });

            // 2. radios (BE / RE): a real click, so the page's resetFunction() runs now,
            //    before any value is put back
            $.each(data, function (name, value) {
                if (name.indexOf('__') === 0 || isSkippedName(name) || typeof value !== 'string') return;
                var el = byName(name);
                if (!el.is(':radio')) return;
                var target = el.filter(function () { return $(this).val() === value; }).first();
                if (target.length && !target.prop('disabled') && !target.is(':checked')) target[0].click();
            });

            // 3. text fields and any other static control
            $.each(data, function (name, value) {
                if (name.indexOf('__') === 0 || isSkippedName(name) || typeof value !== 'string') return;
                if (name === ROOT_SELECT || $.inArray(name, DEPENDENT_SELECTS) !== -1) return;  // step 4
                var el = byName(name);
                if (el.length === 0 || el.is(':radio') || el.is(':checkbox') || el.prop('disabled')) return;
                if (el.is('select')) {
                    if (optionExists(el, value)) el.val(value);        // never blank a select
                } else {
                    el.val(value);
                }
            });

            // 4. Financial Year, then Parent and Reference Budget once their lists are reloaded
            var root = data[ROOT_SELECT] ? { name: ROOT_SELECT, value: data[ROOT_SELECT], done: false, firedPoll: null } : null;
            var deps = [];
            $.each(DEPENDENT_SELECTS, function (i, name) {
                if (data[name]) deps.push({ name: name, value: data[name], done: false, sig: null });
            });
            if (!root && deps.length === 0) { enableSaving(); return; }

            var polls = 0;
            var timer = null;

            function tick() {
                polls++;

                if (root && !root.done) {
                    var rootEl = byName(root.name);
                    if (rootEl.length === 0 || rootEl.prop('disabled')) {
                        root.done = true;                               // locked by the page: leave it alone
                    } else if (optionExists(rootEl, root.value)) {
                        $.each(deps, function (i, s) { s.sig = optionSignature(byName(s.name)); });
                        if (rootEl.val() !== root.value) {
                            rootEl.val(root.value);
                            fireChange(rootEl);                         // page reloads Parent / Reference Budget
                            root.firedPoll = polls;
                        }
                        root.done = true;
                    } else if (polls >= GIVE_UP_POLLS) {
                        root.done = true;
                    }
                }

                if (!root || root.done) {
                    var elapsed = polls - (root && root.firedPoll !== null ? root.firedPoll : 0);
                    $.each(deps, function (i, s) {
                        if (s.done) return;
                        var el = byName(s.name);
                        if (el.length === 0) { s.done = true; return; }
                        // Wait until the page has replaced the list (or enough time has passed).
                        var reloaded = (root && root.firedPoll === null) || !root ||
                            optionSignature(el) !== s.sig || elapsed >= LISTS_READY_POLLS;
                        if (!reloaded) return;
                        if (!el.prop('disabled') && optionExists(el, s.value)) {
                            el.val(s.value);
                            s.done = true;
                        } else if (elapsed >= (el.prop('disabled') ? LISTS_READY_POLLS : GIVE_UP_POLLS)) {
                            s.done = true;                              // locked, or option no longer exists
                        }
                    });
                }

                var allDone = (!root || root.done) && $.grep(deps, function (s) { return !s.done; }).length === 0;
                if (allDone || polls >= CHAIN_MAX_POLLS) {
                    clearInterval(timer);
                    enableSaving();
                }
            }

            timer = setInterval(tick, CHAIN_POLL_MS);
            tick();   // the first step needs no waiting
        }

        // After a server-side validation failure the form already holds the user's data,
        // so a stale draft must not be restored on top of it.
        var serverErrors = $('.error-msg').filter(function () {
            return $.trim($(this).text()) !== '';
        }).length > 0;

        // Silent restore: a saved draft is filled in automatically, with no prompt.
        // restore() turns saving on by itself when it has finished.
        var draft = serverErrors ? null : readDraft(storageKey);
        if (!draft) {
            enableSaving();
        } else {
            state.restored = true;
            try { restore(draft); }
            catch (e) { removeDraft(storageKey); state.restored = false; enableSaving(); }
        }

        /* ---- triggers ---- */

        state.interval = setInterval(save, SAFETY_SAVE_MS);

        formObj.on('input change', ':input', function () {
            if (state.closed) return;
            state.dirty = true;
            scheduleSave();
        });
        $(document).on('visibilitychange', function () {
            if (document.visibilityState === 'hidden') save();
        });
        $(window).on('pagehide beforeunload', save);

        // Clear only when the submit really goes through (validation may cancel it).
        formObj.on('submit', function (e) {
            setTimeout(function () {
                if (!e.isDefaultPrevented()) closeDraft();
            }, 0);
        });

        // Programmatic form.submit() does not fire the submit event.
        var form = formObj[0];
        var nativeSubmit = (typeof form.submit === 'function') ? form.submit : HTMLFormElement.prototype.submit;
        form.submit = function () {
            closeDraft();
            return nativeSubmit.apply(form, arguments);
        };

        $('[id*=discard], [class*=discard]').on('click', closeDraft);

        // Call from your logout link/handler so the next user never sees this draft.
        window.clearBudgetDefinitionDrafts = function () {
            if (!storage) return;
            try {
                for (var i = storage.length - 1; i >= 0; i--) {
                    var k = storage.key(i);
                    if (k && k.indexOf(prefix) === 0) storage.removeItem(k);
                }
            } catch (e) {}
        };
    });
})(jQuery, window);