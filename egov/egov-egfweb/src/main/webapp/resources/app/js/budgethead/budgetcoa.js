function coaGlcode_initialize() {
	 var custom = new Bloodhound({
	    datumTokenizer: function(d) { return d.tokens; },
	    queryTokenizer: Bloodhound.tokenizers.whitespace,
		   remote: {
	            url: '/services/EGF/common/getaccountcodesforaccountdetailtype?glcode=',
	            dataType: "json",
	            filter: function (data) {
	            	var responseObj = JSON.parse(data);
	                return $.map(responseObj, function (ct) {
	                    return {
	                        id: ct.id,
	                        name: ct.name,
	                        glcode: ct.glcode,
	                        glcodesearch: ct.glcode+' ~ '+ct.name
	                    };
	                });
	            }
	        }
   });

   custom.initialize();
   var dt = $('.coaGlcode').typeahead({
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
		   var glcodeid =document.getElementById('tempCoaDetails['+index+'].glcodeid').value;
		   var detailtypeid = document.getElementById('tempCoaDetails['+index+'].detailTypeId').value;
		   var detailkeyid = document.getElementById('tempCoaDetails['+index+'].detailKeyId').value;
			if( glcodeid!= "" && originalglcodeid == glcodeid &&  originaldetailtypeid == detailtypeid && originaldetailkeyid == detailkeyid) {
				flag = true;
			}
	   });
	    if(data.issubledger && originaldetailtypeid!='' && originaldetailkeyid=='')
	    {
		   bootbox.alert($.i18n.prop('msg.please.enter',subLedgerDisplayName),function() {
			    var index= dt.length - 1;
			    if(document.getElementById('tempCoaDetails['+index+'].coaGlcode'))
			    	document.getElementById('tempCoaDetails['+index+'].coaGlcode').value = "";
			});
	    }else if(flag){
			bootbox.alert($.i18n.prop('msg.debit.code.already.added'), function() {
				var index= dt.length - 1;
				if(document.getElementById('tempCoaDetails['+index+'].coaGlcode'))
					document.getElementById('tempCoaDetails['+index+'].coaGlcode').value = "";
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