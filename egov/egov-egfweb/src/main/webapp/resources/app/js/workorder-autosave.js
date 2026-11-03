(function ($, window) {
    'use strict';

    var DRAFT_VERSION = 1;
    var DRAFT_TTL_MS = 24 * 60 * 60 * 1000;
    var DEBOUNCE_MS = 500;
    var SAFETY_SAVE_MS = 30000;               // periodic save; it also removes the draft once 24h have passed
    var CHAIN_POLL_MS = 400;
    var CHAIN_MAX_POLLS = 25;

    // Field names never written to browser storage.
    var secretFieldRegex = /password|csrf|token/i;
    // Input types that carry no user data.
    var skipTypeRegex = /^(button|submit|reset|file|image|password|hidden)$/i;
    // Dependent selects whose options arrive by AJAX, restored one after another.
    var CASCADE_SELECTS = ['fund.id', 'scheme.id', 'subScheme.id'];
    // Static selects that other fields depend on: fire change after restoring.
    var CHANGE_ON_RESTORE = ['contractor'];

    /* ---------- storage helpers (never throw) ---------- */


    var storage = (function () {
        try {
            var t = '__draft_test__';
            window.sessionStorage.setItem(t, t);
            window.sessionStorage.removeItem(t);
            return window.sessionStorage;
        } catch (e) {
            return null;
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

    /** Dispatches a real change event, so handlers bound by jQuery or addEventListener run. */
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

    /* ---------- main ---------- */

    $(function () {
        var formObj = $('#orderNumber, #orderValue').first().closest('form');
        if (formObj.length === 0 || !storage) return;

        // Existing work order (edit/view): the server data is the truth, never a local draft.
        var recordId = $.trim($('input[type="hidden"][name="workOrder"]', formObj).val() || '');
        if (recordId) return;

        var userId = String(formObj.attr('data-draft-user') || 'anonymous');
        var prefix = 'work_order_draft_';
        var storageKey = prefix + userId + '_' + window.location.pathname;

        var state = {
            closed: false,      // true after submit/discard/expiry: no more saving
            enabled: false,     // true after the restore has finished
            baseline: null,     // snapshot of the untouched form (taken before any restore)
            debounce: null,
            interval: null
        };

        function byName(name) {
            return $('[name="' + name.replace(/"/g, '\\"') + '"]', formObj);
        }

        function isSkippedName(name) {
            return !name || secretFieldRegex.test(name);
        }

        function collect() {
            var data = {};
            var checkboxes = {};
            formObj.find(':input').each(function () {
                var el = $(this);
                var name = el.attr('name');
                var type = (el.attr('type') || '').toLowerCase();
                if (isSkippedName(name) || skipTypeRegex.test(type)) return;
                if (el.prop('disabled')) return;               // derived fields, never submitted
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
            var data = collect();
            if (JSON.stringify(data) === state.baseline) { removeDraft(storageKey); return; } // nothing changed
            writeDraft(storageKey, data);
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

        function restore(data) {
            // 1. plain fields, static selects and radios
            $.each(data, function (name, value) {
                if (name.indexOf('__') === 0 || isSkippedName(name) || typeof value !== 'string') return;
                if ($.inArray(name, CASCADE_SELECTS) !== -1) return;   // restored in order, below
                var el = byName(name);
                if (el.length === 0) return;

                if (el.is(':radio')) {
                    // A real click, so the page's own handlers run.
                    var target = el.filter(function () { return $(this).val() === value; }).first();
                    if (target.length && !target.is(':checked')) target[0].click();
                } else if (el.is('select')) {
                    if (optionExists(el, value)) {                      // never blank a select
                        el.val(value);
                        if ($.inArray(name, CHANGE_ON_RESTORE) !== -1) fireChange(el);
                    }
                } else if (!el.is(':checkbox')) {
                    el.val(value);
                }
            });

            // 2. checkboxes (Active), including the unticked state
            $.each(data.__checkboxes || {}, function (key, on) {
                var sep = key.indexOf('|');
                var name = key.substring(0, sep);
                var val = key.substring(sep + 1);
                var cb = byName(name).filter(':checkbox').filter(function () {
                    return $(this).val() === val;
                }).first();
                if (cb.length && !cb.prop('disabled') && cb.is(':checked') !== !!on) cb[0].click();
            });

            // 3. Fund -> Scheme -> Sub Scheme: each list is loaded by AJAX after the
            //    previous one changes, so set one, fire change, wait for the next options.
            var chain = [];
            $.each(CASCADE_SELECTS, function (i, name) {
                if (data[name]) chain.push({ name: name, value: data[name], applied: false });
            });
            if (chain.length === 0) { enableSaving(); return; }

            var idx = 0;
            var polls = 0;
            var timer = null;

            function tick() {
                polls++;
                while (idx < chain.length) {
                    var step = chain[idx];
                    var el = byName(step.name);
                    if (el.length === 0) { idx++; continue; }
                    if (!optionExists(el, step.value)) break;           // options not loaded yet
                    if (!step.applied) {
                        el.val(step.value);
                        fireChange(el);
                        step.applied = true;
                    }
                    idx++;
                }
                if (idx >= chain.length || polls >= CHAIN_MAX_POLLS) {
                    clearInterval(timer);
                    enableSaving();
                }
            }

            timer = setInterval(tick, CHAIN_POLL_MS);
            tick();   // first step needs no waiting
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
            try { restore(draft); }
            catch (e) { removeDraft(storageKey); enableSaving(); }
        }

        /* ---- triggers ---- */

        state.interval = setInterval(save, SAFETY_SAVE_MS);

        formObj.on('input change', ':input', function () {
            if (!state.closed) scheduleSave();
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
        window.clearWorkOrderDrafts = function () {
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