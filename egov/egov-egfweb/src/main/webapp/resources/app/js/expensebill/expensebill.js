/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

var $netPayableAccountCodeId = 0;
var subLedgerDisplayName;
var detailTypeName;
var detailKeyName;
var debitCodes = new Array();
var billamount = 0;
var debitamount = 0;
var creditamount = 0;
var netpayableamount = 0;
var debitAmountrowcount=0;
var creditAmoutrowcount=0;
var accountCodeTemplateMap = {};

$(document).ready(function(){
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
	
	$('#subLedgerType').trigger("change");
	$netPayableAccountCodeId = $('#netPayableId').val();
	patternvalidation(); 
	debitGlcode_initialize();
	creditGlcode_initialize();
	$('#fund').val($('#fund').val());
	loadCheckListTable();
	if($("#mode").val() == 'edit'){
		netpayableamount = $("#netPayableAmount").val();
		billamount = $("#billamount").val();
		creditamount = Number($("#billamount").val()) - Number(netpayableamount);
		debitamount = $("#billamount").val();
		
		$("#expenseNetPayableAmount").html(netpayableamount);
		$("#expenseBillTotalDebitAmount").html(debitamount);
		$("#expenseBillTotalCreditAmount").html(creditamount);
		$("#billamount").val(billamount);
	}
	else{
		calculateBillAmount();
	}
	
	var entityName = new Bloodhound({
		datumTokenizer : function(datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer : Bloodhound.tokenizers.whitespace,
		remote : {
			url : '/services/EGF/common/getentitesbyaccountdetailtype?name=',
			replace: function (url, query) {
				var subLedgerType = $('#subLedgerType').val();
				if(subLedgerType == null || subLedgerType == "")
					bootbox.alert($.i18n.prop('msg.select.subledger.type'));
				return url + query + '&accountDetailType=' + subLedgerType ;
			},
			filter : function(data) {
				return $.map(data, function(ct) {
					return {
						code:ct.split("~")[0].split("-")[0],
						name : ct.split("~")[0].split("-")[1],
						id : ct.split("~")[1],
						codeAndName:ct
					};
				});
			}
		}
	});

	entityName.initialize();
$('#subLedgerCode').typeahead({
		hint : true,
		highlight : true,
		minLength : 3
	}, {
		displayKey : 'codeAndName',
		source : entityName.ttAdapter()
	}).on('typeahead:selected', function (event, data) {
		$("#subLedgerCode").val(data.code);
		detailKeyName = data.name;
		$("#payTo").val(data.name);
		$("#detailkeyId").val(data.id);
	});
});

$('.btn-wf-primary').click(function(){
	var button = $(this).attr('id');
	if (button != null && (button == 'Forward')) {
		if(!validateWorkFlowApprover(button))
			return false;
		if(!$("form").valid())
			return false;
		if(validate()){
			deleteHiddenSubledgerRow();
			return true;
		}else
			return false;
		
	}else if (button != null && (button == 'Create And Approve')) {
		$('#approvalDepartment').removeAttr('required');
		$('#approvalDesignation').removeAttr('required');
		$('#approvalPosition').removeAttr('required');
		$('#approvalComent').removeAttr('required');
		if(!validateWorkFlowApprover(button))
			return false;
		if(!$("form").valid())
			return false;
		if(validate()){
			deleteHiddenSubledgerRow();
			return true;
		}else
			return false;
	} else{
		if(!validateWorkFlowApprover(button))
			return false;
		if($("form").valid()){
			deleteHiddenSubledgerRow();
			return true;
		}else
			return false;
	}
	return false;
});

function getCookie(name){
	let cookies = document.cookie;
	if(cookies.search(name) != -1){
		var keyValue = cookies.match('(^|;) ?' + name + '=([^;]*)(;|$)');
	    return keyValue ? keyValue[2] : null;
	}
}

function getLocale(paramName){
	return getCookie(paramName) ? getCookie(paramName) : navigator.language;
}

function deleteHiddenSubledgerRow(){
	var subLedgerCount = $("#tblsubledgerdetails > tbody > tr:visible[id='subledhgerrow']").length;
	if(subLedgerCount==0){
		deleteRow($(".subLedgerAmount_0")[0],'tblsubledgerdetails');
	}
}
function debitGlcode_initialize() {
	 var custom = new Bloodhound({
	    datumTokenizer: function(d) { return d.tokens; },
	    queryTokenizer: Bloodhound.tokenizers.whitespace,
		   remote: {
	            url: '/services/EGF/common/getaccountcodesforaccountdetailtype?glcode=',
	            dataType: "json",
	            replace: function (url, query) {
					var subLedgerType = $('#subLedgerType').val();
					if(subLedgerType == null || subLedgerType == "")
						subLedgerType = "0";
					if(subLedgerType != null || subLedgerType != "")
					return url + query + '&accountDetailType=' + subLedgerType ;
				},
	            filter: function (data) {
	            	var responseObj = JSON.parse(data);
	                return $.map(responseObj, function (ct) {
	                    return {
	                        id: ct.id,
	                        name: ct.name,
	                        glcode: ct.glcode,
	                        issubledger: ct.isSubLedger,
	                        glcodesearch: ct.glcode+' ~ '+ct.name
	                    };
	                });
	            }
	        }
   });

   custom.initialize();
   var dt = $('.debitGlcode').typeahead({
   	hint : true,
		highlight : true,
		minLength : 3
		
	}, {		    
         displayKey: 'glcodesearch',
         source: custom.ttAdapter()
   }).on('typeahead:selected typeahead:autocompleted', function (event, data) {
	   
	   var originalglcodeid = data.id;
	   var originaldetailtypeid = $('#subLedgerType').val();
	   var originaldetailkeyid = $("#detailkeyId").val();
	   var flag = false;
	   $('#tbldebitdetails  > tbody > tr:visible[id="debitdetailsrow"]').each(function(index) {
		   var glcodeid =document.getElementById('tempDebitDetails['+index+'].glcodeid').value;
		   var detailtypeid = document.getElementById('tempDebitDetails['+index+'].detailTypeId').value;
		   var detailkeyid = document.getElementById('tempDebitDetails['+index+'].detailKeyId').value;
			if( glcodeid!= "" && originalglcodeid == glcodeid &&  originaldetailtypeid == detailtypeid && originaldetailkeyid == detailkeyid) {
				flag = true;
			}
	   });
	    if(data.issubledger && originaldetailtypeid!='' && originaldetailkeyid=='')
	    {
		   bootbox.alert($.i18n.prop('msg.please.enter',subLedgerDisplayName),function() {
			    var index= dt.length - 1;
			    if(document.getElementById('tempDebitDetails['+index+'].debitGlcode'))
			    	document.getElementById('tempDebitDetails['+index+'].debitGlcode').value = "";
			});
	    }else if(flag){
			bootbox.alert($.i18n.prop('msg.debit.code.already.added'), function() {
				var index= dt.length - 1;
				if(document.getElementById('tempDebitDetails['+index+'].debitGlcode'))
					document.getElementById('tempDebitDetails['+index+'].debitGlcode').value = "";
			});
		}else{
			$(this).parents("tr:first").find('.debitdetailname').val(data.name);
		   	$(this).parents("tr:first").find('.debitaccountcode').val(data.glcode);
		   	$(this).parents("tr:first").find('.debitdetailid').val(data.id);
		   	$(this).parents("tr:first").find('.debitIsSubLedger').val(data.issubledger);
			$(this).parents("tr:first").find('.debitDetailTypeId').val($('#subLedgerType').val());
			$(this).parents("tr:first").find('.debitDetailKeyId').val($('#detailkeyId').val());
			$(this).parents("tr:first").find('.debitDetailTypeName').val(detailTypeName);
			$(this).parents("tr:first").find('.debitDetailKeyName').val(detailKeyName);
		}
   });
}

function creditGlcode_initialize() {
	 var custom = new Bloodhound({
	    datumTokenizer: function(d) { return d.tokens; },
	    queryTokenizer: Bloodhound.tokenizers.whitespace,
		   remote: {
	            url: '/services/EGF/common/getaccountcodesforaccountdetailtype?glcode=',
	            dataType: "json",
	            replace: function (url, query) {
					var subLedgerType = $('#subLedgerType').val();
					if(subLedgerType == null || subLedgerType == "")
						subLedgerType = "0";
					return url + query + '&accountDetailType=' + subLedgerType ;
				},
	            filter: function (data) {
	            	var responseObj = JSON.parse(data);
	                return $.map(responseObj, function (ct) {
	                    return {
	                        id: ct.id,
	                        name: ct.name,
	                        glcode: ct.glcode,
	                        issubledger: ct.isSubLedger,
	                        glcodesearch: ct.glcode+' ~ '+ct.name
	                    };
	                });
	            }
	        }
  });

  custom.initialize();

  var dt=$('.creditGlcode').typeahead({
  	hint : true,
		highlight : true,
		minLength : 3
		
	}, {		    
        displayKey: 'glcodesearch',
        source: custom.ttAdapter()
  }).on('typeahead:selected typeahead:autocompleted', function (event, data) {
	  
	  var originalglcodeid = data.id;
	   var originaldetailtypeid = $('#subLedgerType').val();
	   var originaldetailkeyid = $("#detailkeyId").val();
	   var flag = false;
	   $('#tblcreditdetails  > tbody > tr:visible[id="creditdetailsrow"]').each(function(index) {
		   var glcodeid =document.getElementById('tempCreditDetails['+index+'].glcodeid').value;
		   var detailtypeid = document.getElementById('tempCreditDetails['+index+'].detailTypeId').value;
		   var detailkeyid = document.getElementById('tempCreditDetails['+index+'].detailKeyId').value;
			if( glcodeid!= "" && originalglcodeid == glcodeid &&  originaldetailtypeid == detailtypeid && originaldetailkeyid == detailkeyid) {
				flag = true;
			}
	   });
	   if(data.issubledger && originaldetailtypeid!='' && originaldetailkeyid=='')
	    {
		   bootbox.alert($.i18n.prop('msg.please.enter',subLedgerDisplayName),function() {
			   var index= dt.length - 1;
				document.getElementById('tempCreditDetails['+index+'].creditGlcode').value = "";
			});
	    }else if(flag){
			bootbox.alert($.i18n.prop('msg.credit.code.already.added'), function() {
				var index= dt.length - 1;
				document.getElementById('tempCreditDetails['+index+'].creditGlcode').value = "";
			});
		}else{
			$(this).parents("tr:first").find('.creditdetailname').val(data.name);
		  	$(this).parents("tr:first").find('.creditdetailid').val(data.id);    
		  	$(this).parents("tr:first").find('.creditIsSubLedger').val(data.issubledger);
		  	$(this).parents("tr:first").find('.creditaccountcode').val(data.glcode);
		  	$(this).parents("tr:first").find('.creditDetailTypeId').val($('#subLedgerType').val());
			$(this).parents("tr:first").find('.creditDetailKeyId').val($('#detailkeyId').val());
			$(this).parents("tr:first").find('.creditDetailTypeName').val(detailTypeName);
			$(this).parents("tr:first").find('.creditDetailKeyName').val(detailKeyName);
		}
  });
}


$('#subLedgerType').change(function () {
	$('#subLedgerCode').val("");
	//$("#payTo").val("");
	$("#detailkeyId").val("");
	if($('#subLedgerType').val() != ""){
		subLedgerDisplayName=$('#subLedgerType').find(":selected").text() + " Code";
		detailTypeName=$('#subLedgerType').find(":selected").text();
		$("#subLedgerCodeSection").removeClass("display-hide");
		$('#subLedgerNameLabel').html($('#subLedgerType').find(":selected").text() + " Code");
		clearAllDetails();
	}
	else{
		$("#subLedgerCodeSection").addClass("display-hide");
		$('#subLedgerNameLabel').html("Code");
	}
	var accountDetailType ;
	if ($('#subLedgerType').val() === '') {
		accountDetailType = "0";
	}else{
		accountDetailType = $('#subLedgerType').val();
	}
		
		$.ajax({
			method : "GET",
			url : "/services/EGF/common/getnetpayablecodesbyaccountdetailtype",
			dataType: "json",
			data : {
				accountDetailType : accountDetailType
			},
			async : true
		}).done(
				function(response) {
					$('#netPayableAccountCode').empty();
					$('#netPayableAccountCode').append($("<option value=''>Select from below</option>"));
					var responseObj = JSON.parse(response);
					$.each(responseObj, function(index, value) {
						var selected="";
						$('#netPayableAccountCode').append($('<option '+ selected +'>').text(value.glcode + '-' +value.name + '~' + value.isSubLedger).attr('value', value.id));
					});
				});
		
		loadAccountCodeTemplate();
});

$('#billSubType').change(function () {
	$("#selectedCheckList").val("");
	loadCheckListTable();
	loadAccountCodeTemplate();
});

function loadCheckListTable(){
	if($('#billSubType').val()!=""){
		$.ajax({
			method : "GET",
			url : "/services/EGF/common/getchecklistbybillsubtype",
			data : {
				billSubType : $('#billSubType').val()
			},
			async : true
		}).done(
				function(response) {
					$.each(response, function(index, value) {
						$('#tblchecklist tbody').empty();
						var output = '';
						$.each(response, function(index, value) {
							var selected = "";
							var selectedCheckValue = "";
							if($("#selectedCheckList").val()){
							var selectedCheckList = $("#selectedCheckList").val();
							var selectedCheckListArray= selectedCheckList.split(',');
								for (var i in selectedCheckListArray) {
									if(value.id == selectedCheckListArray[i].split('-')[0]){
										selected = "selected";
										selectedCheckValue = selectedCheckListArray[i].split('-')[1];
									}
								}
							}
							output = '<tr id="tblchecklistrow">';
							output = output + '<td class="text-left">' + value.value + '</td>'
							output = output + '<td class="text-right">'
							output = output + '<input id="checkLists['+index+'].appconfigvalue.id" name="checkLists['+index+'].appconfigvalue.id" type="hidden" value="'+ value.id +'"/>'
							output = output + '<select id="checkLists['+index+'].checklistvalue" name="checkLists['+index+'].checklistvalue" data-first-option="false" class="form-control">'
							if(selected!="" && selectedCheckValue == "na")
								output = output + '<option value="na" selected = "selected">N/A</option>'
							else
								output = output + '<option value="na">N/A</option>'
							if(selected!="" && selectedCheckValue == "Yes")
								output = output + '<option value="Yes" selected = "selected">Yes</option>'
							else
								output = output + '<option value="Yes">Yes</option>'
							if(selected!="" && selectedCheckValue == "No")
								output = output + '<option value="No" selected = "selected">No</option>'
							else
								output = output + '<option value="No">No</option>'
							
							output = output + '</select>'
							output = output + '</td>'
							output = output + '</tr>';
							$('#tblchecklist tbody').append(output);
					});
				});
			
		});
	}
}

$('#netPayableAccountCode').change(function () {
	if ($('#netPayableAccountCode').val() != '') {
		var netpayGlcode=$('#netPayableAccountCode option:selected').text();
		$('#netPayableDetailTypeId').val($('#subLedgerType').val());
		$('#netPayableIsSubLedger').val(netpayGlcode.split('~')[1]);
		$('#netPayableDetailKeyId').val($('#detailkeyId').val());
		$('#netPayableDetailTypeName').val(detailTypeName);
		$('#netPayableDetailKeyName').val(detailKeyName);
		$('#netPayableGlcode').val(netpayGlcode.substr(0,netpayGlcode.indexOf('-')));
		$('#netPayableAccountHead').val(netpayGlcode.substr(netpayGlcode.indexOf('-')+1));
		
	}
});
function addDebitDetailsRow() { 
	
	$('.debitGlcode').typeahead('destroy');
	$('.debitGlcode').unbind();
	var rowcount = $("#tbldebitdetails tbody tr").length;
	if (rowcount < 40) {
		if (document.getElementById('debitdetailsrow') != null) {
			addRow('tbldebitdetails','debitdetailsrow');
			$('#tbldebitdetails tbody tr:eq('+rowcount+')').find('.debitDetailGlcode').val('');
			$('#tbldebitdetails tbody tr:eq('+rowcount+')').find('.debitdetailname').val('');
			$('#tbldebitdetails tbody tr:eq('+rowcount+')').find('.debitAmount').val('');
			$('#tbldebitdetails tbody tr:eq('+rowcount+')').blur(calcualteNetpaybleAmount);
			debitGlcode_initialize();
			++debitAmountrowcount;
			addCustomEvent(rowcount,'tempDebitDetails[index].debitamount','keydown',shortKeyFunForAltIComb);
			addCustomEvent(rowcount,'tempDebitDetails[index].addButton','keydown',shortKeyFunForAddButton);
		}
	} else {
		  bootbox.alert($.i18n.prop('msg.limit.reached'));
	}
}

function deleteDebitDetailsRow(obj) {
	var rowcount=$("#tbldebitdetails tbody tr").length;
    if(rowcount<=1) {
		bootbox.alert($.i18n.prop('msg.this.row.can.not.be.deleted'));
		return false;
	} else if (confirm("Are you sure you want to Delete")) {
		deleteRow(obj,'tbldebitdetails');
		--debitAmountrowcount;
		return true;
	}else{
		return false
	}
    
    resetDebitCodes();
}

function addCreditDetailsRow() { 
	
	$('.creditGlcode').typeahead('destroy');
	$('.creditGlcode').unbind();
	var rowcount = $("#tblcreditdetails tbody tr").length;
	if (rowcount < 40) {
		if (document.getElementById('creditdetailsrow') != null) {
			addRow('tblcreditdetails','creditdetailsrow');
			$('#tblcreditdetails tbody tr:eq('+rowcount+')').find('.creditDetailGlcode').val('');
			$('#tblcreditdetails tbody tr:eq('+rowcount+')').find('.creditdetailname').val('');
			$('#tblcreditdetails tbody tr:eq('+rowcount+')').find('.creditAmount').val('');
			$('#tblcreditdetails tbody tr:eq('+rowcount+')').find('.creditAmount').blur(calcualteNetpaybleAmount);
			creditGlcode_initialize();
			++creditAmoutrowcount;
			addCustomEvent(rowcount,'tempCreditDetails[index].creditamount','keydown',shortKeyFunForAltIComb);
			addCustomEvent(rowcount,'tempCreditDetails[index].addButton','keydown',shortKeyFunForAddButton);
		}
	} else {
		bootbox.alert($.i18n.prop('msg.limit.reached'));
	}
}

function deleteCreditDetailsRow(obj) {
	var rowcount=$("#tblcreditdetails tbody tr").length;
    if(rowcount<=1) {
    	bootbox.alert($.i18n.prop('msg.this.row.can.not.be.deleted'));
		return false;
	} else if (confirm("Are you sure you want to Delete")) {
		deleteRow(obj,'tblcreditdetails');
		--creditAmoutrowcount;
		return true;
	} else{
    	return false;
	}
}


function deleteAccountDetails(obj) {
	var index = obj.id.split('_')[1];
	var glcodeid = document.getElementById('accountDetailsGlCodeId_'+index).value;
	if($netPayableAccountCodeId && $netPayableAccountCodeId==glcodeid)
	{
			$netPayableAccountCodeId=0;
			netpayableamount = 0;
	}
	var rowcount=$("#tblaccountdetails tbody tr").length;
    if(rowcount<=1) {
    	$('#accountdetailsrow').prop("hidden","true");
		$('#accountdetailsrow').prop('accountdetailsinvisible',"true");
		$('#accountDetailsGlCodeId_0').val("");    
	    $('.accountDetailsGlCode_0').html("");
		$('.accountDetailsAccountHead_0').html("");
		$('.accountDetailsDebitAmount_0').html("");
		$('.accountDetailsCreditAmount_0').html("");
		$('#accountDetailsDebitAmount_0').val("");
		$('#accountDetailsCreditAmount_0').val("");
		$('.accountDetailsDebitDetailTypeId_0').val("");
		$('.accountDetailsDebitDetailKeyId_0').val("");
		deleteSubLedgerDetails(glcodeid);
		calculateBillAmount();
	} else {
		deleteSubLedgerDetails(glcodeid);
		deleteRow(obj,'tblaccountdetails');
		calculateBillAmount();
		return true;
	}	
}

function deleteSubLedgerDetails(glcodeid) {
	$('#tblsubledgerdetails  > tbody > tr:visible[id="subledhgerrow"]').each(function() {
		if($(this).find(".subLedgerGlCodeIdId").val()==glcodeid){
			var rowcount=$("#tblsubledgerdetails tbody tr").length;
		    if(rowcount<=1) {
		    	$('#subledhgerrow').prop("hidden","true");
		    	$('#subledhgerrow').prop('subledgerinvisible',"true");
		    	$('#subLedgerGlCodeId_0').val("");  
		    	$('#subLedgerIsDebit_0').val("");
		    	$('#subLedgerDebitAmount_0').val("");
		    	$('#subLedgerCreditAmount_0').val("");
		        $('.subLedgerGlCode_0').html("");
		        $('#subLedgerDetailTypeId_0').val("");
		    	$('#subLedgerDetailKeyId_0').val("");
		    	$('.subLedgerType_0').html("");
		    	$('.subLedgerName_0').html("");
		    	$('.subLedgerAmount_0').html("");
			}else{
				deleteRow($(this).find(".subLedgerAmount")[0],'tblsubledgerdetails');
			}
		}
			
	});
}


function validateAccountDetails()
{	
	var flag = true;
	
	$('#tbldebitdetails  > tbody > tr:visible[id="debitdetailsrow"]').each(function(index) {
		if($(this).find(".debitdetailid").val()!='' && $(this).find(".debitAmount").val()=='')
		{
			bootbox.alert($.i18n.prop('msg.please.enter.debit.amount',(index+1)));
			flag = false;
			return false;
		}
		if(!validateDuplicateAccountDetail($(this).find(".debitdetailid").val(),$(this).find(".debitDetailTypeId").val(),
				$(this).find(".debitDetailKeyId").val())){
			bootbox.alert($.i18n.prop('msg.already.added.debit.detail', $(this).find(".debitaccountcode").val()));
			flag = false;
			return false;
		}
	});
	
	$('#tblcreditdetails  > tbody > tr:visible[id="creditdetailsrow"]').each(function(index) {
		if($(this).find(".creditdetailid").val()!='' && $(this).find(".creditAmount").val()=='')
		{
			bootbox.alert($.i18n.prop('msg.please.enter.credit.amount',(index+1)));
			flag = false;
			return false;
		}
		
		if(!validateDuplicateAccountDetail($(this).find(".creditdetailid").val(),$(this).find(".creditDetailTypeId").val(),
				$(this).find(".creditDetailKeyId").val())){
			bootbox.alert($.i18n.prop('msg.already.added.credit.detail', $(this).find(".creditaccountcode").val()));
			flag = false;
			return false;
		}
	});
	
	
	$('#tblnetpayable  > tbody > tr:visible[id="netpayablerow"]').each(function() {
		if($(this).find("#netPayableAccountCode").val()!='' && $netPayableAccountCodeId){
			bootbox.alert($.i18n.prop('msg.only.one.net.payable.account.can.select'));
			flag = false;
			return false
		}
		if($(this).find("#netPayableAccountCode").val()!='' && $(this).find("#netPayableAmount").val()=='')
		{
			bootbox.alert($.i18n.prop('msg.please.enter.net.apyable.amount'));
			flag = false;
			return false
		}
	});
	
	
	return flag;
}
function validateDuplicateAccountDetail(glcodeid,detailtypeid,detailkeyid){
	var existInSL = false;
	var flag= true;
	$('#tblsubledgerdetails  > tbody > tr:visible[id="subledhgerrow"]').each(function() {
		if(glcodeid!="" && $(this).find(".subLedgerGlCodeIdId").val()==glcodeid){
			existInSL = true;
		}
			
		if (glcodeid!="" && $(this).find(".subLedgerGlCodeIdId").val() ==glcodeid && $(this).find(".subLedgerDetailTypeId").val()==detailtypeid && $(this).find(".subLedgerDetailKeyId").val()==detailkeyid)
		{
			flag = false;
			return false
		}
	});
	if(!existInSL){
		$('#tblaccountdetails  > tbody > tr:visible[id="accountdetailsrow"]').each(function() {
			if( glcodeid!="" && $(this).find(".accountDetailsGlCodeId").val() == glcodeid)
			{
				flag = false;
				return false
			}
		});
	}
	return flag;
}
$("#populateAccountDetails").click(function () {
	var isMerged= false;
	var accountDetailsCount = $("#tblaccountdetails > tbody > tr:visible[id='accountdetailsrow']").length;
	if(validateAccountDetails())
		{
		$('#tbldebitdetails  > tbody > tr:visible[id="debitdetailsrow"]').each(function() {
			 if($(this).find(".debitdetailid").val()!='' && $(this).find(".debitAmount").val()!='')
				{
				 isMerged= false;
				 var glcodeid = $(this).find(".debitdetailid").val();
				 var currentdebitamount = $(this).find(".debitAmount").val();
					if(accountDetailsCount!=0){
						$('#tblaccountdetails  > tbody > tr:visible[id="accountdetailsrow"]').each(function() {
							if($(this).find(".accountDetailsGlCodeId").val() == glcodeid)
							{
								$(this).find(".debitamount").val(parseFloat(Number($(this).find(".debitamount").val())+Number(currentdebitamount)).toFixed());
								$(this).find(".accountDetailsDebitAmount").html($(this).find(".debitamount").val());
								isMerged = true;
							}
						});
						if(!isMerged){
							addRow('tblaccountdetails', 'accountdetailsrow');
						}
					}
					if(!isMerged){
						$('#accountdetailsrow').removeAttr("hidden");
						$('#accountdetailsrow').removeAttr('accountdetailsinvisible');
						$('#accountDetailsGlCodeId_' + accountDetailsCount).val($(this).find(".debitdetailid").val());    
					    $('.accountDetailsGlCode_' + accountDetailsCount).html($(this).find(".debitaccountcode").val());
						$('.accountDetailsAccountHead_' + accountDetailsCount).html($(this).find(".debitdetailname").val());
						$('.accountDetailsDebitAmount_' + accountDetailsCount).html(Number($(this).find(".debitAmount").val()).toFixed(2));
						$('.accountDetailsCreditAmount_' + accountDetailsCount).html('0.00');
						$('#accountDetailsDebitAmount_' + accountDetailsCount).val(Number($(this).find(".debitAmount").val()).toFixed(2));
						$('#accountDetailsCreditAmount_' + accountDetailsCount).val('0.00');
						$('.accountDetailsDebitDetailTypeId_' + accountDetailsCount).val($(this).find(".debitDetailTypeId").val());
						$('.accountDetailsDebitDetailKeyId_' + accountDetailsCount).val($(this).find(".debitDetailKeyId").val());
						accountDetailsCount++;
					}
					if($(this).find(".debitIsSubLedger").val()!="false" && $(this).find(".debitDetailTypeId").val()!="" && $(this).find(".debitDetailKeyId").val()!=""){
						populateSubLedgerDetails($(this).find(".debitdetailid").val(),$(this).find(".debitaccountcode").val(),
								$(this).find(".debitDetailTypeId").val(),$(this).find(".debitDetailKeyId").val(),
								$(this).find(".debitDetailTypeName").val(),$(this).find(".debitDetailKeyName").val(),
								$(this).find(".debitAmount").val(),true);
					}
			 }
		 });
		$('#tblcreditdetails  > tbody > tr:visible[id="creditdetailsrow"]').each(function() {
			if($(this).find(".creditdetailid").val()!='' && $(this).find(".creditAmount").val()!='')
			{
				isMerged= false;
				var glcodeid = $(this).find(".creditdetailid").val();
				var currentcreditamount = $(this).find(".creditAmount").val();
				if(accountDetailsCount!=0){
					$('#tblaccountdetails  > tbody > tr:visible[id="accountdetailsrow"]').each(function() {
						if($(this).find(".accountDetailsGlCodeId").val() == glcodeid)
						{
							$(this).find(".creditamount").val(parseFloat(Number($(this).find(".creditamount").val())+Number(currentcreditamount)).toFixed());
							$(this).find(".accountDetailsCreditAmount").html($(this).find(".creditamount").val());
							isMerged = true;
						}
					});
					if(!isMerged){
						addRow('tblaccountdetails', 'accountdetailsrow');
					}
				}
				if(!isMerged){
					$('#accountdetailsrow').removeAttr("hidden");
					$('#accountdetailsrow').removeAttr('accountdetailsinvisible');
					$('#accountDetailsGlCodeId_' + accountDetailsCount).val($(this).find(".creditdetailid").val());    
				    $('.accountDetailsGlCode_' + accountDetailsCount).html($(this).find(".creditaccountcode").val());
					$('.accountDetailsAccountHead_' + accountDetailsCount).html($(this).find(".creditdetailname").val());
					$('.accountDetailsDebitAmount_' + accountDetailsCount).html('0.00');
					$('.accountDetailsCreditAmount_' + accountDetailsCount).html(Number($(this).find(".creditAmount").val()).toFixed(2));
					$('#accountDetailsDebitAmount_' + accountDetailsCount).val('0.00');
					$('#accountDetailsCreditAmount_' + accountDetailsCount).val(Number($(this).find(".creditAmount").val()).toFixed(2));
					$('.accountDetailsDebitDetailTypeId_' + accountDetailsCount).val($(this).find(".creditDetailTypeId").val());
					$('.accountDetailsDebitDetailKeyId_' + accountDetailsCount).val($(this).find(".creditDetailKeyId").val());
					accountDetailsCount++;
				}
				if($(this).find(".creditIsSubLedger").val()!="false"  && $(this).find(".creditDetailTypeId").val()!="" && $(this).find(".creditDetailKeyId").val()!=""){
					populateSubLedgerDetails($(this).find(".creditdetailid").val(),$(this).find(".creditaccountcode").val(),
							$(this).find(".creditDetailTypeId").val(),$(this).find(".creditDetailKeyId").val(),
							$(this).find(".creditDetailTypeName").val(),$(this).find(".creditDetailKeyName").val(),
							$(this).find(".creditAmount").val(),false);
				}
			}
		 });
		
		$('#tblnetpayable  > tbody > tr:visible[id="netpayablerow"]').each(function() {
			if($(this).find("#netPayableAccountCode").val()!='' && $(this).find("#netPayableAmount").val()!='')
			{
				flag= false;
				if(accountDetailsCount!=0){
					addRow('tblaccountdetails', 'accountdetailsrow');
				}
				netpayableamount =  parseFloat(Number($(this).find("#expense-netPayableAmount").val())).toFixed(2);
				$('#accountdetailsrow').removeAttr("hidden");
				$('#accountdetailsrow').removeAttr('accountdetailsinvisible');
				$('#accountDetailsGlCodeId_' + accountDetailsCount).val($(this).find("#netPayableAccountCode").val());    
			    $('.accountDetailsGlCode_' + accountDetailsCount).html($(this).find("#netPayableGlcode").val());
				$('.accountDetailsAccountHead_' + accountDetailsCount).html($(this).find("#netPayableAccountHead").val().split("~")[0]);
				$('.accountDetailsDebitAmount_' + accountDetailsCount).html('0.00');
				$('.accountDetailsCreditAmount_' + accountDetailsCount).html(Number($(this).find("#expense-netPayableAmount").val()).toFixed(2));
				$('#accountDetailsDebitAmount_' + accountDetailsCount).val('0.00');
				$('#accountDetailsCreditAmount_' + accountDetailsCount).val(Number($(this).find("#expense-netPayableAmount").val()).toFixed(2));
				$('.accountDetailsDebitDetailTypeId_' + accountDetailsCount).val($(this).find("#netPayableDetailTypeId").val());
				$('.accountDetailsDebitDetailKeyId_' + accountDetailsCount).val($(this).find("#netPayableDetailKeyId").val());
				$netPayableAccountCodeId = $(this).find("#netPayableAccountCode").val();
				$("#netPayableId").val($(this).find("#netPayableAccountCode").val());
				accountDetailsCount++;
				if($(this).find("#netPayableIsSubLedger").val()!="false"  && $(this).find("#netPayableDetailTypeId").val()!="" && $(this).find("#netPayableDetailKeyId").val()!=""){
					populateSubLedgerDetails($(this).find("#netPayableAccountCode").val(),$(this).find("#netPayableGlcode").val(),
							$(this).find("#netPayableDetailTypeId").val(),$(this).find("#netPayableDetailKeyId").val(),
							$(this).find("#netPayableDetailTypeName").val(),$(this).find("#netPayableDetailKeyName").val(),
							$(this).find("#expense-netPayableAmount").val(),false);
				}
			}
		 });
		clearAllDetails();
	}
	calculateBillAmount();
});

addCustomEvent('0','tempDebitDetails[index].debitamount','keydown',shortKeyFunForAltIComb);
addCustomEvent('0','tempCreditDetails[index].creditamount','keydown',shortKeyFunForAltIComb);
addCustomEvent('0','tempDebitDetails[index].addButton','keydown',shortKeyFunForAddButton);
addCustomEvent('0','tempCreditDetails[index].addButton','keydown',shortKeyFunForAddButton);

function shortKeyFunForAltIComb (zEvent) {
	var currId = zEvent.target.id;
    if (currId.endsWith('.debitamount') && zEvent.altKey  &&  zEvent.key === "i") {  // case sensitive
    	zEvent.preventDefault ();
        addDebitDetailsRow();
    }else if (currId.endsWith('.creditamount') && zEvent.altKey  &&  zEvent.key === "i") {  // case sensitive
    	zEvent.preventDefault ();
    	addCreditDetailsRow();
    }
    zEvent.stopPropagation ();
}
function shortKeyFunForAddButton (zEvent) {
	var currId = zEvent.target.id;
	if(currId.startsWith('tempDebitDetails') && zEvent.keyCode == 32){
		zEvent.preventDefault ();
    	addDebitDetailsRow();
    }else if(currId.startsWith('tempCreditDetails') && zEvent.keyCode == 32){
    	zEvent.preventDefault ();
    	addCreditDetailsRow();
    }
//	$('[data-toggle="tooltip"]').tooltip("hide");
    zEvent.stopPropagation ();
}

function addCustomEvent(index,target,type,func){
	target = target.replace('index',index);
	addCustomEventListener(target, type, func);
}

function addCustomEventListener(target,type,func){
	var targArea = document.getElementById (target);
	if(targArea != null){
		targArea.addEventListener(type,  func);	
	}
}


function validate(){
	if(billamount == 0){
		bootbox.alert($.i18n.prop('msg.please.select.account.details'));
		return false;
	}
	
	if(debitamount != Number(Number(creditamount) + Number(netpayableamount))){
		bootbox.alert($.i18n.prop('msg.debit.and.credit.amount.is.not.matching'));
		return false;
	}
	
	if(debitamount == 0){
		bootbox.alert($.i18n.prop('msg.please.select.atleast.one.debit.details'));
		return false;
	}
	
	if(!$netPayableAccountCodeId)
	{
		bootbox.alert($.i18n.prop('msg.please.select.one.net.payable.account.detail'));
		return false;
	}
	return true;
}

function clearAllDetails() {
	var debitDetailsCount = $("#tbldebitdetails > tbody > tr:visible[id='debitdetailsrow']").length;
	for (var i = debitDetailsCount; i >= 1; i--) {
		if(1 == i){
			document.getElementById('tempDebitDetails[0].debitGlcode').value = "";
			document.getElementById('tempDebitDetails[0].glcode').value = "";
			document.getElementById('tempDebitDetails[0].glcodeid').value = "";
			document.getElementById('tempDebitDetails[0].detailTypeId').value = "";
			document.getElementById('tempDebitDetails[0].detailKeyId').value = "";
			document.getElementById('tempDebitDetails[0].debitAccountHead').value = "";
			document.getElementById('tempDebitDetails[0].debitamount').value = "";
		}else{
			var objects = $('.debit-delete-row');
			deleteRow(objects[i-1],'tbldebitdetails');
		}
	}
	
	var creditDetailsCount = $("#tblcreditdetails > tbody > tr:visible[id='creditdetailsrow']").length;
	for (var i = creditDetailsCount; i >= 1; i--) {
		if(1 == i){
			document.getElementById('tempCreditDetails[0].creditGlcode').value = "";
			document.getElementById('tempCreditDetails[0].glcode').value = "";
			document.getElementById('tempCreditDetails[0].glcodeid').value = "";
			document.getElementById('tempCreditDetails[0].detailTypeId').value = "";
			document.getElementById('tempCreditDetails[0].detailKeyId').value = "";
			document.getElementById('tempCreditDetails[0].creditAccountHead').value = "";
			document.getElementById('tempCreditDetails[0].creditamount').value = "";
		}else{
			var objects = $('.credit-delete-row');
			deleteRow(objects[i-1],'tblcreditdetails');
		}
	}
	
	$("#netPayableAccountId").val("");
	$("#netPayableAccountCodeId").val("");
	$("#netPayableDetailTypeId").val("");
	$("#netPayableDetailKeyId").val("");
	$("#netPayableAccountCode").val("");
	$("#expense-netPayableAmount").val("");
	
	
	
}


function populateSubLedgerDetails(glCodeId,glCode,detailTypeId,detailKeyId,detailTypeName,detailKeyName,amount,isdebit){
	
	var subLedgerCount = $("#tblsubledgerdetails > tbody > tr:visible[id='subledhgerrow']").length;
	if(subLedgerCount!=0){
		addRow('tblsubledgerdetails', 'subledhgerrow');
	}
	$('#subledhgerrow').removeAttr("hidden");
	$('#subledhgerrow').removeAttr('subledgerinvisible');
	$('#subLedgerGlCodeId_' + subLedgerCount).val(glCodeId);  
	$('#subLedgerIsDebit_' + subLedgerCount).val(isdebit);
	if(isdebit){
		$('#subLedgerDebitAmount_' + subLedgerCount).val(amount);
		$('#subLedgerCreditAmount_' + subLedgerCount).val("0");
	}else{
		$('#subLedgerDebitAmount_' + subLedgerCount).val("0");
		$('#subLedgerCreditAmount_' + subLedgerCount).val(amount);
	}
    $('.subLedgerGlCode_' + subLedgerCount).html(glCode);
    $('#subLedgerDetailTypeId_' + subLedgerCount).val(detailTypeId);
	$('#subLedgerDetailKeyId_' + subLedgerCount).val(detailKeyId);
	$('.subLedgerType_' + subLedgerCount).html(detailTypeName);
	$('.subLedgerName_' + subLedgerCount).html(detailKeyName);
	$('.subLedgerAmount_' + subLedgerCount).html(amount);
	
}

function calculateBillAmount(){
	billamount = 0;
	debitamount = 0;
	creditamount = 0;
	$('#tblaccountdetails  > tbody > tr:visible[id="accountdetailsrow"]').each(function() {
		billamount = parseFloat(Number(billamount) + Number($(this).find(".accountDetailsDebitAmount").html())).toFixed(2);
		debitamount = parseFloat(Number(debitamount) + Number($(this).find(".accountDetailsDebitAmount").html())).toFixed(2);
		creditamount = parseFloat(Number(creditamount) + Number($(this).find(".accountDetailsCreditAmount").html())).toFixed(2);
	});
	creditamount = amountConverter(creditamount - netpayableamount);
	$("#expenseNetPayableAmount").html(netpayableamount);
	$("#expenseBillTotalDebitAmount").html(debitamount);
	$("#expenseBillTotalCreditAmount").html(creditamount);
	$("#billamount").val(billamount);
}

function validateWorkFlowApprover(name) {
	document.getElementById("workFlowAction").value = name;
	var button = document.getElementById("workFlowAction").value;
	if (button != null && button == 'Submit') {
		$('#approvalDepartment').attr('required', 'required');
		$('#approvalDesignation').attr('required', 'required');
		$('#approvalPosition').attr('required', 'required');
		$('#approvalComent').removeAttr('required');
	}
	if (button != null && button == 'Reject') {
		$('#approvalDepartment').removeAttr('required');
		$('#approvalDesignation').removeAttr('required');
		$('#approvalPosition').removeAttr('required');
		$('#approvalComent').attr('required', 'required');
	}
	if (button != null && button == 'Cancel') {
		$('#approvalDepartment').removeAttr('required');
		$('#approvalDesignation').removeAttr('required');
		$('#approvalPosition').removeAttr('required');
		$('#approvalComent').attr('required', 'required');
	}
	if (button != null && button == 'Forward') {
		$('#approvalDepartment').attr('required', 'required');
		$('#approvalDesignation').attr('required', 'required');
		$('#approvalPosition').attr('required', 'required');
		$('#approvalComent').removeAttr('required');
	}
	if (button != null && button == 'Approve') {
		$('#approvalComent').removeAttr('required');
	}
	if (button != null && button == 'Create And Approve') {
		return validateCutOff();
	}else
		return true;
	
	return true;
}
function validateCutOff()
{
	var cutofdate = $("#cutOffDate").val();
	var billdate = $("#billdate").val();
	var cutOffDateArray=cutofdate.split("/");
	var billDateArray=billdate.split("/");
	var cutOffDate = new Date(cutOffDateArray[1] + "/" + cutOffDateArray[0] + "/"
			+ cutOffDateArray[2]);
	var billDate = new Date(billDateArray[1] + "/" + billDateArray[0] + "/"
			+ billDateArray[2]);
	if(billDate<=cutOffDate)
	{
		return true;
	}
	else
	{
		bootbox.alert($.i18n.prop('msg.cutoff.warnig.message',cutofdate));
		return false;
	}
	return false;
}
function calcualteNetpaybleAmount(){
	

	var debitamt = 0;
	var creditamt = 0;
	// var debitAmountrowcount=0;
	// var creditAmoutrowcount=0;
	for (var count = 0; count <=debitAmountrowcount; ++count) {

		if (null != document.getElementById("tempDebitDetails[" + count
				+ "].debitamount")) {
			var val = document.getElementById("tempDebitDetails[" + count
					+ "].debitamount").value;
			if (val != "" && !isNaN(val)) {
				debitamt = parseFloat(Number(debitamt) + Number(val)).toFixed(2);
			}
		}
	}

	for (var count = 0; count <=creditAmoutrowcount; ++count) {

		if (null != document.getElementById("tempCreditDetails[" + count
				+ "].creditamount")) {
			var val = document.getElementById("tempCreditDetails[" + count
					+ "].creditamount").value;
			if (val != "" && !isNaN(val)) {
				creditamt = parseFloat(Number(creditamt) + Number(val)).toFixed(2);
			}
		}
	}
	netPayableAmount=amountConverter(debitamt-creditamt);
	$("#expense-netPayableAmount").val(netPayableAmount);
	$("#expenseNetPayableAmount").html(netPayableAmount);
	$("#expenseBillTotalDebitAmount").html(debitamt);
	$("#expenseBillTotalCreditAmount").html(creditamt);
//	document.getElementById("expense-netPayableAmount").value= amountConverter(debitamt-creditamt);
}
function amountConverter(amt) {
	var formattedAmt = amt.toFixed(2);
	return formattedAmt;
}

function loadAccountCodeTemplate(){
	if($('#billSubType').val()){
		$.ajax({
			method : "GET",
			url : "/services/EGF/accountCodeTemplate/list",
			data : {
				billSubType : $('#billSubType').val() ? $('#billSubType').find(":selected").text() : "",
				module: 'ExpenseBill',
				detailTypeName : $('#subLedgerType').val()  ? $('#subLedgerType').find(":selected").text()  : "",
				detailTypeId: $('#subLedgerType').val() ? $('#subLedgerType').val() : 0
			},
			async : true
		}).done(
				function(response) {
					accountCodeTemplateMap = {}
					var output = '<option value>Select</option>';
					$('#accountCodeTemplateId').empty();
					$.each(response, function(index, value) {
						accountCodeTemplateMap[value.code] = value; 
						output = output + '<option value="'+value.code+'">'+value.code +' - '+value.name+'</option>'
				});
					$('#accountCodeTemplateId').append(output);
		});
	}
}

$('#accountCodeTemplateId').change(function () {
	var selectedTemp = $(this).val();
	console.log("current1 : ",$.data(this, 'current'));
	if($(this).val()){
		populateAccountCodeTemplateDetails(selectedTemp);
	}
});


function populateAccountCodeTemplateDetails(selectedTemp){
	clearAllDetails();
	var accTempDet = accountCodeTemplateMap[selectedTemp];
	$.each(accTempDet.debitCodeDetails, function(index, value) {
		$('.debitGlcode').typeahead('destroy');
		$('.debitGlcode').unbind();
		$('#tbldebitdetails tbody tr:eq('+index+')').find('.debitDetailGlcode').val(value.glcode+' ~ '+value.name);
		$('#tbldebitdetails tbody tr:eq('+index+')').find('.debitdetailname').val(value.name);
		$('#tbldebitdetails tbody tr:eq('+index+')').find('.debitaccountcode').val(value.glcode);
		$('#tbldebitdetails tbody tr:eq('+index+')').find('.debitdetailid').val(value.id);
		$('#tbldebitdetails tbody tr:eq('+index+')').find('.debitAmount').val("0");
		$('#tbldebitdetails tbody tr:eq('+index+')').find('.debitDetailTypeName').val(detailTypeName);
		$('#tbldebitdetails tbody tr:eq('+index+')').find('.debitDetailKeyName').val(detailKeyName);
		$('#tbldebitdetails tbody tr:eq('+index+')').find('.debitIsSubLedger').val(value.isSubledger ? true : false);
		$('#tbldebitdetails tbody tr:eq('+index+')').find('.debitDetailTypeId').val($('#subLedgerType').val());
		$('#tbldebitdetails tbody tr:eq('+index+')').find('.debitDetailKeyId').val($('#detailkeyId').val());
		debitGlcode_initialize();
		if(++index < accTempDet.debitCodeDetails.length)
			addDebitDetailsRow();
	});
	$.each(accTempDet.creditCodeDetails, function(index, value) {
		$('.creditGlcode').typeahead('destroy');
		$('.creditGlcode').unbind();
		$('#tblcreditdetails tbody tr:eq('+index+')').find('.creditDetailGlcode').val(value.glcode+' ~ '+value.name);
		$('#tblcreditdetails tbody tr:eq('+index+')').find('.creditdetailname').val(value.name);
		$('#tblcreditdetails tbody tr:eq('+index+')').find('.creditaccountcode').val(value.glcode);
		$('#tblcreditdetails tbody tr:eq('+index+')').find('.creditdetailid').val(value.id);
		$('#tblcreditdetails tbody tr:eq('+index+')').find('.creditAmount').val("0");
		$('#tblcreditdetails tbody tr:eq('+index+')').find('.creditDetailTypeName').val(detailTypeName);
		$('#tblcreditdetails tbody tr:eq('+index+')').find('.creditDetailKeyName').val(detailKeyName);
		$('#tblcreditdetails tbody tr:eq('+index+')').find('.creditIsSubLedger').val(value.isSubLedger);
		$('#tblcreditdetails tbody tr:eq('+index+')').find('.creditDetailTypeId').val($('#subLedgerType').val());
		$('#tblcreditdetails tbody tr:eq('+index+')').find('.creditDetailKeyId').val($('#detailkeyId').val());
		creditGlcode_initialize();
		if(++index < accTempDet.creditCodeDetails.length)
			addCreditDetailsRow();
	});
	if($("#netPayableAccountCode option[value="+accTempDet.netPayable.id+"]").length==1){
		$('#netPayableAccountCode').val(accTempDet.netPayable.id);
		$('#netPayableDetailTypeId').val($('#subLedgerType').val());
		$('#netPayableIsSubLedger').val(accTempDet.netPayable.isSubLedger);
		$('#netPayableDetailKeyId').val($('#detailkeyId').val());
		$('#netPayableDetailTypeName').val(detailTypeName);
		$('#netPayableDetailKeyName').val(detailKeyName);
		$('#netPayableGlcode').val(accTempDet.netPayable.glcode);
		$('#netPayableAccountHead').val(accTempDet.netPayable.name+'~'+(accTempDet.netPayable.isSubledger? 'true':'false'));
	}
}

$("#accountCodeTemplateId").focus(function() {
	if( $("#accountCodeTemplateId > option").length <= 1 ) {
		$("#accountCodeTempEmptyMessage").css("display", "block");
	}
});

$("#accountCodeTemplateId").blur(function() {
	if( $("#accountCodeTemplateId > option").length <= 1 ) {
		$("#accountCodeTempEmptyMessage").css("display", "none");
	}
});







(function ($, window) {
	'use strict';

	var DRAFT_VERSION = 1;
	var DRAFT_TTL_MS = 24 * 60 * 60 * 1000;   // draft is removed 24 hours after it was first saved
	var DEBOUNCE_MS = 500;
	var SAFETY_SAVE_MS = 30000;               // periodic save; it also removes the draft once 24h have passed
	var RESTORE_POLL_MS = 500;
	var RESTORE_MAX_POLLS = 20;

	// Names never stored: secrets, and workflow dropdowns loaded by another script.
	var workflowFieldRegex = /password|csrf|token|approvalDesignation|approvalPosition|approverName/i;
	// Input types that carry no user data.
	var skipTypeRegex = /^(button|submit|reset|file|image|password|hidden)$/i;
	// Entry rows, net payable block and template dropdown are rebuilt by other code.
	var skipDetailsRegex = /^(tempDebitDetails|tempCreditDetails|checkLists)\[|netPayable|accountCodeTemplate/i;
	// Fields saved and restored explicitly, in a fixed order.
	var SPECIAL_FIELDS = '#subLedgerType, #subLedgerCode, #detailkeyId, #billSubType';
	// Anything inside these tables is skipped by the generic field loop.
	var DETAIL_TABLES = '#tbldebitdetails, #tblcreditdetails, #tblnetpayable, #tblaccountdetails, #tblsubledgerdetails, #tblchecklist';

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

	/* ---------- main ---------- */

	$(function () {
		var formObj = $('#subLedgerType, #billSubType').first().closest('form');
		if (formObj.length === 0 || !storage) return;
		// Edit mode loads the saved bill from the server; an old local draft must not overwrite it.
		if (String($('#mode').val() || '').toLowerCase() === 'edit') return;

		var userId = String(formObj.attr('data-draft-user') || 'anonymous');
		var prefix = 'expense_bill_draft_';
		var storageKey = prefix + userId + '_' + window.location.pathname;

		var state = {
			closed: false,      // true after submit/discard/expiry: no more saving
			enabled: false,     // true after restore has finished
			baseline: null,     // snapshot of the untouched form (taken before any restore)
			debounce: null,
			interval: null
		};

		function byName(name) {
			return $('[name="' + name.replace(/"/g, '\\"') + '"]', formObj);
		}

		function isSkipped(el, name, type) {
			if (el.is(SPECIAL_FIELDS) || el.hasClass('tt-hint') || el.closest(DETAIL_TABLES).length) return true;
			if (!name || workflowFieldRegex.test(name)) return true;
			if (skipDetailsRegex.test(name) || skipDetailsRegex.test(el.attr('id') || '')) return true;
			return skipTypeRegex.test(type);
		}

		/** Checklist answers as { appConfigValueId: 'Yes' | 'No' | 'na' }. */
		function collectChecklist() {
			var out = {};
			$('#tblchecklist tbody tr').each(function () {
				var row = $(this);
				var id = row.find('input[type="hidden"][name$=".appconfigvalue.id"]').val();
				var val = row.find('select').val();
				if (id) out[id] = val || '';
			});
			return out;
		}

		function collect() {
			var data = {};
			formObj.find(':input').each(function () {
				var el = $(this);
				var name = el.attr('name');
				var type = (el.attr('type') || '').toLowerCase();
				if (isSkipped(el, name, type)) return;
				if ((type === 'radio' || type === 'checkbox') && !el.is(':checked')) return;
				data[name] = el.val();
			});
			data.__billSubType = $('#billSubType').val() || '';
			data.__subLedgerType = $('#subLedgerType').val() || '';
			data.__subLedgerCode = $('#subLedgerCode').val() || '';
			data.__detailKeyId = $('#detailkeyId').val() || '';
			data.__detailKeyName = window.detailKeyName || '';
			data.__checklist = collectChecklist();
			data.__designation = $('#approvalDesignation').val() || '';
			data.__position = $('#approvalPosition').val() || '';
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

		/** Sets a select only when the option exists. Returns true when it was set. */
		function setSelect(selector, value) {
			var el = $(selector);
			if (!value || el.length === 0) return false;
			var has = el.find('option').filter(function () { return $(this).val() === value; }).length > 0;
			if (!has) return false;
			el.val(value);
			return true;
		}

		function restore(parsed) {
			// 1. plain fields
			$.each(parsed, function (name, value) {
				if (name.indexOf('__') === 0 || skipDetailsRegex.test(name)) return;
				var el = byName(name);
				if (el.length === 0 || el.is(SPECIAL_FIELDS) || el.closest(DETAIL_TABLES).length) return;
				if (el.is(':radio') || el.is(':checkbox')) {
					el.filter(function () { return $(this).val() === value; }).prop('checked', true);
				} else {
					el.val(value);
				}
			});

			// 2. bill sub type and sub ledger type (selects rendered by the server)
			var hasBillSubType = setSelect('#billSubType', parsed.__billSubType);
			var hasSubLedgerType = setSelect('#subLedgerType', parsed.__subLedgerType);

			// 3. the sub ledger type change handler clears code and key and reloads
			//    the net payable list and the template list, so fill code/key afterwards.
			if (hasSubLedgerType) {
				$('#subLedgerType').trigger('change');
				$('#subLedgerCode').val(parsed.__subLedgerCode || '');
				$('#detailkeyId').val(parsed.__detailKeyId || '');
				if (parsed.__detailKeyName) window.detailKeyName = parsed.__detailKeyName;
			}

			// 4. checklist: the page reads #selectedCheckList ("id-value,id-value") when it
			//    builds the table, so set it and let the page's own function do the work.
			var checklist = parsed.__checklist || {};
			var checklistIds = Object.keys(checklist);
			if (hasBillSubType) {
				$('#selectedCheckList').val($.map(checklistIds, function (id) {
					return id + '-' + checklist[id];
				}).join(','));
				if (typeof window.loadCheckListTable === 'function') window.loadCheckListTable();
				if (!hasSubLedgerType && typeof window.loadAccountCodeTemplate === 'function') {
					window.loadAccountCodeTemplate();
				}
			}
			var waitForChecklist = hasBillSubType && checklistIds.length > 0;

			// 5. workflow approver
			if (parsed.__designation) $('#approvalDesignationValue').val(parsed.__designation);
			if (parsed.__position) $('#approvalPositionValue').val(parsed.__position);
			if (parsed.approvalDepartment) {
				$('#approvalDepartment').val(parsed.approvalDepartment).trigger('change');
			}

			// 6. other selects whose options arrive by AJAX
			var pending = {};
			$.each(parsed, function (name, value) {
				if (name.indexOf('__') === 0 || !value || workflowFieldRegex.test(name)) return;
				if (skipDetailsRegex.test(name)) return;
				var el = byName(name);
				if (el.length && el.is('select') && !el.is(SPECIAL_FIELDS) && !el.closest(DETAIL_TABLES).length) {
					pending[name] = value;
				}
			});

			var polls = 0;
			var timer = setInterval(function () {
				polls++;
				var remaining = 0;

				$.each(pending, function (name, value) {
					var el = byName(name);
					if (el.length === 0 || el.val() === value) { delete pending[name]; return; }
					var has = el.find('option').filter(function () { return $(this).val() === value; }).length > 0;
					if (has) { el.val(value).trigger('change'); delete pending[name]; }
					else remaining++;
				});

				// Do not start saving before the checklist rows exist, or the saved answers
				// would be overwritten with an empty list.
				if (waitForChecklist && $('#tblchecklist tbody tr').length === 0) remaining++;

				if (remaining === 0 || polls >= RESTORE_MAX_POLLS) {
					clearInterval(timer);
					enableSaving();
				}
			}, RESTORE_POLL_MS);
		}

		// Silent restore: a saved draft is filled in automatically, with no prompt.
		// restore() turns saving on by itself once the AJAX-filled fields have loaded.
		var draft = readDraft(storageKey);
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
		window.clearExpenseBillDrafts = function () {
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