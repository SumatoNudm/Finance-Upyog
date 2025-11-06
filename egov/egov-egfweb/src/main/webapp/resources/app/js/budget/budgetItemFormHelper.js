$(document).ready(function () {
    $.i18n.properties({
        name: 'message',
        path: '/services/EGF/resources/app/messages/',
        mode: 'both',
        async: true,
        cache: true,
        language: getLocale("locale"),
        callback: function () {
            console.log('File loaded successfully');
        }
    });

    budgethead_initialize();
});

function getCookie(name) {
    let cookies = document.cookie;
    if (cookies.search(name) != -1) {
        var keyValue = cookies.match('(^|;) ?' + name + '=([^;]*)(;|$)');
        return keyValue ? keyValue[2] : null;
    }
}

function getLocale(paramName) {
    return getCookie(paramName) ? getCookie(paramName) : navigator.language;
}

function budgethead_initialize() {
    var custom = new Bloodhound({
        datumTokenizer: Bloodhound.tokenizers.obj.whitespace('code', 'name'),
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: '/services/EGF/budgethead/ajaxBudgetHead?query=%QUERY',
            wildcard: '%QUERY',
            dataType: "json",
            transform: function (response) {
                // Response is already parsed JSON (array of BudgetHead)
                console.log(response);
                return $.map(response, function (ct) {
                    return {
                        id: ct.id,
                        name: ct.name,
                        code: ct.code,
                        accountType: ct.accountType,
                        accountTypeCode: ct.accountTypeCode,
                        program: ct.program,
                        category: ct.category
                    };
                });
            }
        }
    });

    custom.initialize();

    $('.budgetcode').typeahead(
        {
            hint: true,
            highlight: true,
            minLength: 2 // show suggestions faster
        },
        {
            name: 'budgethead',
            display: function (item) {
                return item.code + ' - ' + item.name;
            },
            source: custom.ttAdapter(),
            limit: 20,
            templates: {
                suggestion: function (data) {
                    return `<div>${data.code} - ${data.name}</div>`;
                }
            }
        }
    ).on('typeahead:selected typeahead:autocompleted', function (event, data) {
        // console.log("Selected data:", data);
        // console.log("Selected event:", event);

        var originalBudgetHeadcode = data.code;
        var functionCode = document.getElementById("functionCode")?.value;
        var budgetHeadId = data.id;


        $('#dynamicTable  > tbody > tr:visible[id="budgetdetailsrow"]').each(function (index) {
            var budgetheadcode = document.getElementById('items[' + index + '].budgetheadcode');
            var budgetcode = document.getElementById('items[' + index + '].budgetCode');
            var budgetgroup = document.getElementById('items[' + index + '].budgetGroup');
            var budgetheadid = document.getElementById('items[' + index + '].budgetHeadId');

            budgetheadcode.value = originalBudgetHeadcode;
            budgetcode.value = functionCode + '-' + originalBudgetHeadcode;
            budgetgroup.value = getBudgetGroup(data.accountTypeCode);
            budgetheadid.value = budgetHeadId;
        });
    });
}

function addBudgetDetailsRow() {
    $('.budgetcode').typeahead('destroy');
    $('.budgetcode').unbind();
    var rowcount = $("#dynamicTable tbody tr").length;
    if (rowcount < 40) {
        if (document.getElementById('budgetdetailsrow') != null) {
            addRow('dynamicTable', 'budgetdetailsrow');
            $('#dynamicTable tbody tr:eq(' + rowcount + ')').find('.budgetHeadcode').val('');
            // $('#dynamicTable tbody tr:eq(' + rowcount + ')').find('.debitdetailname').val('');
            budgethead_initialize();
            addCustomEvent(rowcount, 'items[index].addButton', 'keydown', shortKeyFunForAddButton);
        }
    } else {
        bootbox.alert($.i18n.prop('msg.limit.reached'));
    }
}

// function addBudgetDetailsRow() {
//     // Destroy previous typeahead bindings
//     $('.budgetcode').typeahead('destroy').off();

//     var rowcount = $("#dynamicTable tbody tr").length;

//     // Check if the template row exists
//     var $templateRow = $('#budgetdetailsrow');
//     if ($templateRow.length) {
//         // Clone the template row and remove the ID to prevent duplicates
//         var newRow = $templateRow.clone().removeAttr('id');

//         customIndex++ ;
//         var newIndex = customIndex;

//         console.log("my new index");

//         newRow.find("[id],[name],[data-idx]").each(function () {

//             // Update id attributes
//             if ($(this).attr("id")) {
//                 $(this).attr("id", $(this).attr("id").replace(/\[\d+\]/, "[" + newIndex + "]"));
//             }

//             // Update name attributes
//             if ($(this).attr("name")) {
//                 $(this).attr("name", $(this).attr("name").replace(/\[\d+\]/, "[" + newIndex + "]"));
//             }

//         });

//         // Insert the new row before the closing balance row if it exists
//         var $closingRow = $('#closingBalancerow');
//         if ($closingRow.length) {
//             newRow.insertBefore($closingRow);
//         } else {
//             // If closing balance row is missing, just append it to the end
//             $('#dynamicTable tbody').append(newRow);
//         }

//         // Clear input values and reinitialize features
//         newRow.find('.budgetHeadcode').val('');
//         budgethead_initialize();

//         // Add custom keyboard event to the new row
//         addCustomEvent(rowcount, 'items[index].addButton', 'keydown', shortKeyFunForAddButton);
//     }
// }

function deleteBudgetDetailsRow(obj) {
    var rowcount = $("#dynamicTable tbody tr").length;
    if (rowcount <= 1) {
        bootbox.alert($.i18n.prop('msg.this.row.can.not.be.deleted'));
        return false;
    } else if (confirm("Are you sure you want to Delete")) {
        deleteRow(obj, 'dynamicTable');
        return true;
    } else {
        return false
    }
}

function shortKeyFunForAddButton(zEvent) {
    var currId = zEvent.target.id;
    if (currId.startsWith('items') && zEvent.keyCode == 32) {
        zEvent.preventDefault();
        addBudgetDetailsRow();
    }
    zEvent.stopPropagation();
}

function getBudgetGroup(code) {
    const budgetMap = {
        RR: 'Revenue_Budget',
        RE: 'Revenue_Budget',
        CR: 'Capital_Budget',
        CE: 'Capital_Budget'
    };

    return budgetMap[code] || 'Unknown';
}
