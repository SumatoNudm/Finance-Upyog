
$(document).ready(function () {
    $.i18n.properties({ 
		name: 'message', 
		path: '/services/EGF/resources/app/messages/', 
		mode: 'both',
		async: true,
	    cache: true,
		language: getLocale("locale"),
		callback: function() {
			console.log('File loaded successfully');
		}
	});

    // coaGlcode_initialize();
    debitGlcode_initialize();

});

function debitGlcode_initialize() {
    var custom = new Bloodhound({
        datumTokenizer: function (d) { return d.tokens; },
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: '/services/EGF/common/getaccountcodesforaccountdetailtype?glcode=',
            dataType: "json",
            replace: function (url, query) {
                var subLedgerType = $('#subLedgerType').val();
                if (subLedgerType == null || subLedgerType == "")
                    subLedgerType = "0";
                if (subLedgerType != null || subLedgerType != "")
                    return url + query + '&accountDetailType=' + subLedgerType;
            },
            filter: function (data) {
                var responseObj = JSON.parse(data);
                return $.map(responseObj, function (ct) {
                    return {
                        id: ct.id,
                        name: ct.name,
                        glcode: ct.glcode,
                        issubledger: ct.isSubLedger,
                        glcodesearch: ct.glcode + ' ~ ' + ct.name
                    };
                });
            }
        }
    });

    custom.initialize();
    var dt = $('.debitGlcode').typeahead({
        hint: true,
        highlight: true,
        minLength: 3

    }, {
        displayKey: 'glcodesearch',
        source: custom.ttAdapter()
    }).on('typeahead:selected typeahead:autocompleted', function (event, data) {

        var originalglcodeid = data.id;
        var originaldetailtypeid = $('#subLedgerType').val();
        var originaldetailkeyid = $("#detailkeyId").val();
        var flag = false;
        $('#tbldebitdetails  > tbody > tr:visible[id="debitdetailsrow"]').each(function (index) {
            var glcodeid = document.getElementById('tempDebitDetails[' + index + '].glcodeid').value;
            var detailtypeid = document.getElementById('tempDebitDetails[' + index + '].detailTypeId').value;
            var detailkeyid = document.getElementById('tempDebitDetails[' + index + '].detailKeyId').value;
            if (glcodeid != "" && originalglcodeid == glcodeid && originaldetailtypeid == detailtypeid && originaldetailkeyid == detailkeyid) {
                flag = true;
            }
        });
        if (data.issubledger && originaldetailtypeid != '' && originaldetailkeyid == '') {
            bootbox.alert($.i18n.prop('msg.please.enter', subLedgerDisplayName), function () {
                var index = dt.length - 1;
                if (document.getElementById('tempDebitDetails[' + index + '].debitGlcode'))
                    document.getElementById('tempDebitDetails[' + index + '].debitGlcode').value = "";
            });
        } else if (flag) {
            bootbox.alert($.i18n.prop('msg.debit.code.already.added'), function () {
                var index = dt.length - 1;
                if (document.getElementById('tempDebitDetails[' + index + '].debitGlcode'))
                    document.getElementById('tempDebitDetails[' + index + '].debitGlcode').value = "";
            });
        } else {
            $(this).parents("tr:first").find('.debitdetailname').val(data.name);
            $(this).parents("tr:first").find('.debitaccountcode').val(data.glcode);
            $(this).parents("tr:first").find('.debitdetailid').val(data.id);
            // $(this).parents("tr:first").find('.debitIsSubLedger').val(data.issubledger);
            // $(this).parents("tr:first").find('.debitDetailTypeId').val($('#subLedgerType').val());
            // $(this).parents("tr:first").find('.debitDetailKeyId').val($('#detailkeyId').val());
            // $(this).parents("tr:first").find('.debitDetailTypeName').val(detailTypeName);
            // $(this).parents("tr:first").find('.debitDetailKeyName').val(detailKeyName);
        }
    });
}

function addDebitDetailsRow() {

    $('.debitGlcode').typeahead('destroy');
    $('.debitGlcode').unbind();
    var rowcount = $("#tbldebitdetails tbody tr").length;
    if (rowcount < 40) {
        if (document.getElementById('debitdetailsrow') != null) {
            addRow('tbldebitdetails', 'debitdetailsrow');
            $('#tbldebitdetails tbody tr:eq(' + rowcount + ')').find('.debitDetailGlcode').val('');
            $('#tbldebitdetails tbody tr:eq(' + rowcount + ')').find('.debitdetailname').val('');
            debitGlcode_initialize();
            addCustomEvent(rowcount, 'tempDebitDetails[index].addButton', 'keydown', shortKeyFunForAddButton);
        }
    } else {
        bootbox.alert($.i18n.prop('msg.limit.reached'));
    }
}

function deleteDebitDetailsRow(obj) {
    var rowcount = $("#tbldebitdetails tbody tr").length;
    if (rowcount <= 1) {
        bootbox.alert($.i18n.prop('msg.this.row.can.not.be.deleted'));
        return false;
    } else if (confirm("Are you sure you want to Delete")) {
        deleteRow(obj, 'tbldebitdetails');
        --debitAmountrowcount;
        return true;
    } else {
        return false
    }

    resetDebitCodes();
}
function shortKeyFunForAddButton (zEvent) {
	var currId = zEvent.target.id;
	if(currId.startsWith('tempDebitDetails') && zEvent.keyCode == 32){
		zEvent.preventDefault ();
    	addDebitDetailsRow();
    }
//	$('[data-toggle="tooltip"]').tooltip("hide");
    zEvent.stopPropagation ();
}

// function populateAccountCodeTemplateDetails(selectedTemp){
// 	clearAllDetails();
// 	var accTempDet = accountCodeTemplateMap[selectedTemp];
// 	$.each(accTempDet.debitCodeDetails, function(index, value) {
// 		$('.debitGlcode').typeahead('destroy');
// 		$('.debitGlcode').unbind();
// 		$('#tbldebitdetails tbody tr:eq('+index+')').find('.debitDetailGlcode').val(value.glcode+' ~ '+value.name);
// 		$('#tbldebitdetails tbody tr:eq('+index+')').find('.debitdetailname').val(value.name);
// 		$('#tbldebitdetails tbody tr:eq('+index+')').find('.debitaccountcode').val(value.glcode);
// 		$('#tbldebitdetails tbody tr:eq('+index+')').find('.debitdetailid').val(value.id);
// 		$('#tbldebitdetails tbody tr:eq('+index+')').find('.debitAmount').val("0");
// 		$('#tbldebitdetails tbody tr:eq('+index+')').find('.debitDetailTypeName').val(detailTypeName);
// 		$('#tbldebitdetails tbody tr:eq('+index+')').find('.debitDetailKeyName').val(detailKeyName);
// 		$('#tbldebitdetails tbody tr:eq('+index+')').find('.debitIsSubLedger').val(value.isSubledger ? true : false);
// 		$('#tbldebitdetails tbody tr:eq('+index+')').find('.debitDetailTypeId').val($('#subLedgerType').val());
// 		$('#tbldebitdetails tbody tr:eq('+index+')').find('.debitDetailKeyId').val($('#detailkeyId').val());
// 		debitGlcode_initialize();
// 		if(++index < accTempDet.debitCodeDetails.length)
// 			addDebitDetailsRow();
// 	});
// }