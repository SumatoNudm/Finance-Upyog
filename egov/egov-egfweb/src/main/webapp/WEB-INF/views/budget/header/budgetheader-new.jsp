<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- Budget Header Create Page -->
<div class="container">

    <h3>Create Budget</h3>

    <form id="budgetForm"
          action="${pageContext.request.contextPath}/budget/header/create"
          method="post"
          class="form-horizontal">

        <div class="main-content" >


        <!-- Budget Name -->
        <div class="form-group">
            <label for="name" class="col-sm-2 control-label mb-8">Budget Name</label>
            <div class="col-sm-6">
                <input type="text"
                       id="name"
                       name="name"
                       class="form-control"
                       value="${budgetHeader.name}"
                       required />
            </div>
        </div>

        <!-- Current FY and Next FY side by side -->
        <div class="row">

            <!-- Current Financial Year -->
            <div class="col-sm-4">
                <div class="form-group">
                    <label class="control-label">Current Financial Year</label>
                    <input type="text"
                           class="form-control"
                           value="${currentFy.finYearRange}"
                           readonly />

                    <!-- ID that will be saved in budget_header.currentfinancialyearid -->
                    <input type="hidden"
                           name="currentfinancialyearid"
                           value="${currentFy.id}" />
                </div>
            </div>

            <!-- Next Financial Year -->
            <div class="col-sm-4">
                <div class="form-group">
                    <label class="control-label">Next Financial Year</label>
                    <input type="text"
                           class="form-control"
                           value="${nextFy.finYearRange}"
                           readonly />

                    <!-- ID that will be saved in budget_header.financialyearid -->
                    <input type="hidden"
                           name="financialyearid"
                           value="${nextFy.id}" />
                </div>
            </div>

        </div>

        <!-- Submit Button -->
        <div class="form-group mt-8">
            <div class="col-sm-6 col-sm-offset-2 mt-8">
                <button type="submit" class="btn btn-primary">
                    Submit
                </button>
            </div>
        </div>

        </div>

    </form>
</div>
