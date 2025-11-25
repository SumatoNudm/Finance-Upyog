<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<form:form role="form" action="form" modelAttribute="budgetRegister" id="budgetRegisterForm"
           cssClass="form-horizontal form-groups-bordered" enctype="multipart/form-data">


    <div class="main-content">

        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible" role="alert">
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                ${error}
            </div>
        </c:if>

        <div class="row">
            <div class="col-md-12">
                <div class="panel panel-primary" data-collapsed="0">
                    <div class="panel-heading">
                        <div class="panel-title">
                            <spring:message code="lbl.budget.input" text="Create Budget Register" />
                        </div>
                    </div>

                    <div class="position_alert col-md-10 mx-auto">
                        <c:if test="${not empty errors}">
                            <div class="alert alert-danger py-2 px-3 mb-0 text-center">
                                <c:out value="${errors}" />
                            </div>
                        </c:if>
                    </div>

                    <div class="panel-body">

                        <spring:message code="lbl.createBudgetRegister" text="Create Budget Register" />

                        <div class="form-group">
                            <label class="col-sm-3 control-label text-right">
                                <spring:message code="lbl.budgetRegister" text="Name" /> <span class="mandatory"></span>
                            </label>

                            <div class="col-sm-6 add-margin">
                                <form:input type="text" path="budgetRegister.budgetRegisterName" name="budgetRegister.budgetRegisterName" id="budgetRegister.budgetRegisterName" class="form-control"
                                             required="required" />

                            </div>
                        </div>
                    </div>

                    <!-- CURRENT & NEXT FY SIDE BY SIDE -->
                    <div class="row">

                        <!-- Current FY -->
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-6 control-label">Current Financial Year</label>
                                <div class="col-sm-6">
                                    <input type="text"
                                           class="form-control"
                                           value="${budgetRegister.currentFinancialYear.finYearRange}"
                                           readonly />

                                    <form:hidden path="budgetRegister.currentFinancialYear.id"/>
                                </div>
                            </div>
                        </div>

                        <!-- Next FY -->
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-6 control-label">Next Financial Year</label>
                                <div class="col-sm-6">
                                    <input type="text"
                                           class="form-control"
                                           value="${budgetRegister.financialYear.finYearRange}"
                                           readonly />

                                    <form:hidden path="budgetRegister.financialYear.id"/>
                                </div>
                            </div>
                        </div>

                    </div>


                    <div class="form-group text-center">
                        <c:if test="${empty errors}">
                            <button type='submit' class='btn btn-primary' id="buttonNext">
                                <spring:message code='lbl.submit' text="Next" />
                            </button>
                        </c:if>


                    </div>

                </div>
            </div>
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