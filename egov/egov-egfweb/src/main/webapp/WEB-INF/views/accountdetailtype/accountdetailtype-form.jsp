<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="main-content">
<%@ include file="/includes/tutorial.jsp" %>
    <div class="row">
       <div class="col-md-12">
          <div class="panel panel-primary" data-collapsed="0">
             <div class="panel-heading">
                <div class="panel-title"><spring:message code="lbl.account.entity" text="Account Entity"/> </div>
             </div>
             <div class="panel-body">

                <div class="form-group">
                   <label class="col-sm-3 control-label text-right">
                      <spring:message code="lbl.name" text="Name"/><span class="mandatory"></span>
                   </label>
                   <div class="col-sm-3 add-margin">
                      <form:input path="name"
                         class="form-control text-left patternvalidation"
                         data-pattern="alphanumeric" maxlength="50" required="required" />
                      <form:errors path="name" cssClass="error-msg" />
                   </div>
                   <label class="col-sm-3 control-label text-right">
                      <spring:message code="lbl.description" text="Description"/><span class="mandatory"></span>
                   </label>
                   <div class="col-sm-3 add-margin">
                      <form:input path="description"
                         class="form-control text-left patternvalidation"
                         data-pattern="alphanumeric" maxlength="50" required="required" />
                      <form:errors path="description" cssClass="error-msg" />
                   </div>
                </div>

                <div class="form-group">
                   <label class="col-sm-3 control-label text-right">
                      <spring:message code="lbl.isactive" text="Active"/>
                   </label>
                   <div class="col-sm-3 add-margin">
                      <form:checkbox path="isactive" />
                      <form:errors path="isactive" cssClass="error-msg" />
                   </div>

                   <label class="col-sm-3 control-label text-right">
                      <spring:message code="lbl.account.code" text="Account Code"/><span class="mandatory"></span>
                   </label>
                   <div class="col-sm-3 add-margin" style="position:relative;">
                      <input type="text" id="coaCode" class="form-control" autocomplete="off"
                             placeholder="Type first letter of Account code" required="required" />
                      <!-- pre-filled with the saved chart of accounts id -->
                      <input type="hidden" name="coaId" id="coaId"
                             value="<c:out value='${selectedCoaId}'/>" />
                      <ul id="coaSuggest" class="list-group"
                          style="display:none; position:absolute; z-index:1000; left:15px; right:15px; max-height:220px; overflow-y:auto; margin:0;"></ul>

                      <ul id="coaData" style="display:none;">
                         <c:forEach var="acc" items="${coa}">
                            <li data-id="<c:out value='${acc.id}'/>"
                                data-code="<c:out value='${acc.glcode}'/>"
                                data-name="<c:out value='${acc.name}'/>"></li>
                         </c:forEach>
                      </ul>
                   </div>
                </div>

                <input type="hidden" name="id" value="${accountdetailtype.id}" />
                <input type="hidden" name="accountdetailtype" value="${accountdetailtype.id}" />



<script>
$(document).ready(function () {
   var MIN = 1;
   var items = $('#coaData li').map(function () {
      return { id: String($(this).data('id')), code: String($(this).data('code')), name: String($(this).data('name')) };
   }).get();

   // EDIT: show the saved account in the text box
   var selectedId = $.trim($('#coaId').val());
   if (selectedId) {
      var saved = items.filter(function (i) { return i.id === selectedId; })[0];
      if (saved) { $('#coaCode').val(saved.code + ' - ' + saved.name); }
   }

   $('#coaCode').on('input', function () {
      var q = $.trim($(this).val()).toLowerCase();
      var $list = $('#coaSuggest').empty();
      $('#coaId').val('');                    // typing again invalidates the earlier pick
      if (q.length < MIN) { $list.hide(); return; }

      var matches = items.filter(function (i) {
         return i.code.toLowerCase().indexOf(q) === 0 || i.name.toLowerCase().indexOf(q) !== -1;
      }).slice(0, 50);

      if (!matches.length) { $list.hide(); return; }
      $.each(matches, function (_, i) {
         $('<li class="list-group-item" style="cursor:pointer;"></li>')
            .text(i.code + ' - ' + i.name)
            .data('item', i)
            .appendTo($list);
      });
      $list.show();
   });

   $('#coaSuggest').on('click', 'li', function () {
      var i = $(this).data('item');
      $('#coaCode').val(i.code + ' - ' + i.name);
      $('#coaId').val(i.id);
      $('#coaSuggest').hide();
   });

   $(document).on('click', function (e) {
      if (!$(e.target).closest('#coaCode, #coaSuggest').length) { $('#coaSuggest').hide(); }
   });

   // block submit if the typed text was never picked from the list
   $('#coaCode').closest('form').on('submit', function (e) {
      if (!$('#coaId').val()) {
         e.preventDefault();
         bootbox.alert('Please select an Account Code from the list');
      }
   });
});
</script>