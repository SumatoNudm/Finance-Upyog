<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/tags/cdn.tld" prefix="cdn" %>
<form:form role="form" action="form" modelAttribute="function" id="budgetItemFunction"
  cssClass="form-horizontal form-groups-bordered" enctype="multipart/form-data">
  </div>
  </div>
  </div>
  </div>
  <div class="form-group">
    <div class="text-center">
      <button type='submit' class='btn btn-primary' id="buttonNext">
        <spring:message code='lbl.next' text="Next" />
      </button>

      <div class="form-group">
        <label class="col-sm-3 control-label text-right">
          <spring:message code="lbl.function" text="Function" /> <span class="mandatory"></span>
        </label>
        <div class="col-sm-3 add-margin">
            <form:input path="" name="function" id="function" class="form-control"
              placeholder="Type first 3 letters of Function name" required="required" />

          <form:hidden path="id" name="functionId" id="function"
            class="form-control table-input hidden-input cfunction" />
          <form:errors path="id" cssClass="add-margin error-msg" />
        </div>
      </div>

      <a href='javascript:void(0)' class='btn btn-default'
        onclick="window.parent.postMessage('close','*');window.close();">
        <spring:message code='lbl.close' text="Close" /></a>
    </div>
  </div>
</form:form>
<script>
  $('#buttonNext').click(function (e) {
    if ($('form').valid()) {} else {
      e.preventDefault();
    }
  });
</script>
<script type="text/javascript" src="<cdn:url value='/resources/app/js/budgetGroupHelper.js?rnd=${app_release_no}'/>">
</script>
<script
  src="<cdn:url value='/resources/app/js/i18n/jquery.i18n.properties.js?rnd=${app_release_no}' context='/services/EGF'/>">
</script>

<script src="<cdn:url value='/resources/app/js/common/helper.js?rnd=${app_release_no}' context='/services/EGF'/>">
</script>

<script src="<cdn:url value='/resources/app/js/common/budgetItemHelper.js' context='/services/EGF'/>"></script>