<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/tags/cdn.tld" prefix="cdn" %>
<form:form role="form" action="create" modelAttribute="budgetItem" id="budgetItemform"
  cssClass="form-horizontal form-groups-bordered" enctype="multipart/form-data">
  <%@ include file="budgetitem-form.jsp"%>
  </div>
  </div>
  </div>
  </div>
  <div class="form-group">
    <div class="text-center">
      <button type='submit' class='btn btn-primary' id="buttonSubmit">
        <spring:message code='lbl.create' text="Create" />
      </button>
      <a href='javascript:void(0)' class='btn btn-default'
        onclick="window.parent.postMessage('close','*');window.close();">
        <spring:message code='lbl.close' text="Close" /></a>
    </div>
  </div>
</form:form>
<script>
  $('#buttonSubmit').click(function (e) {
    if ($('form').valid()) {} else {
      e.preventDefault();
    }
  });
</script>
<script type="text/javascript" src="<cdn:url value='/resources/app/js/budgetGroupHelper.js?rnd=${app_release_no}'/>">
</script>
<script
        src="<cdn:url value='/resources/app/js/i18n/jquery.i18n.properties.js?rnd=${app_release_no}' context='/services/EGF'/>"></script>

<script
        src="<cdn:url value='/resources/app/js/common/helper.js?rnd=${app_release_no}' context='/services/EGF'/>"></script>

<script
        src="<cdn:url value='/resources/app/js/common/budgetItemHelper.js' context='/services/EGF'/>"></script>