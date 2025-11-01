<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/tags/cdn.tld" prefix="cdn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    java.util.Calendar cal = java.util.Calendar.getInstance();
    int year = cal.get(java.util.Calendar.YEAR);
    int month = cal.get(java.util.Calendar.MONTH) + 1; // January = 0

    int startYear, endYear;
    if (month < 4) { // before April => FY started last year
        startYear = year - 1;
        endYear = year;
    } else { // April or later => FY starts this year
        startYear = year;
        endYear = year + 1;
    }

    String currentFY = startYear + "-" + String.valueOf(endYear).substring(2);
    String nextFY = (startYear + 1) + "-" + String.valueOf(endYear + 1).substring(2);
%>

<form:form role="form" action="create" modelAttribute="function" id="budgetItemFunction"
	cssClass="form-horizontal form-groups-bordered" enctype="multipart/form-data">

	<div class="main-content">
		<div class="row">
			<div class="col-md-12">
				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading">
						<div class="panel-title">
							<!-- <spring:message code="lbl.budget.input" text="Budget Input" /> -->
							 Function:
							${function.name} (<span class="text-muted">${function.code}</span>)
							<input type="hidden" id="functionCode" name="functionCode" value="${function.code}" />
						</div>
					</div>

					<div class="panel-body">
						<!-- Function Info -->
						<!-- <div class="col-sm-9 add-margin pb-6">
							<strong>Function:</strong>
							${function.name} (<span class="text-muted">${function.code}</span>)
							<input type="hidden" id="functionCode" name="functionCode" value="${function.code}" />
						</div> -->

						<!-- Opening Table -->
						<table class="table table-bordered" id="dynamicTable">
							<thead>
								<tr>
									<th>Budget Head</th>
									<th>BE <strong><%= currentFY %></strong></th>
									<th>Actuals <strong><%= currentFY %></strong> (9 months)</th>
									<th>RE <strong><%= currentFY %></strong></th>
									<th>BE <strong><%= nextFY %></strong></th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td style="width: 30%;">Opening Balance as on 01.04.<%= startYear %></td>
									<td style="width: 15%;"><input type="text" name="items[0].value1"
											class="form-control"></td>
									<td style="width: 15%;"><input type="text" name="items[0].value2"
											class="form-control"></td>
									<td style="width: 15%;"><input type="text" name="items[0].value3"
											class="form-control"></td>
									<td style="width: 15%;"><input type="text" name="items[0].value4"
											class="form-control"></td>
									<td style="width: 10%;"></td>
								</tr>

								<tr id="budgetdetailsrow">
									<td style="width: 30%;">
										<input type="text" id="tempBudgetDetails[0].budgetcode"
											name="tempBudgetDetails[0].budgetcode"
											class="form-control table-input budgetHeadcode budgetcode"
											data-errormsg="Budget Code is mandatory!" data-idx="0" data-optional="0"
											placeholder="Type first 3 letters of Budget code">
									</td>
									
									<form:hidden path="" name="tempBudgetDetails[0].budgetheadcode"
										id="tempBudgetDetails[0].budgetheadcode"
										class="form-control table-input hidden-input budgetheadcode" />
									<form:hidden path="" name="tempBudgetDetails[0].budgetCode"
										id="tempBudgetDetails[0].budgetCode"
										class="form-control table-input hidden-input budgetCode" />
									<td style="width: 15%;"><input type="text" name="items[1].value1"
											class="form-control"></td>
									<td style="width: 15%;"><input type="text" name="items[1].value2"
											class="form-control"></td>
									<td style="width: 15%;"><input type="text" name="items[1].value3"
											class="form-control"></td>
									<td style="width: 15%;"><input type="text" name="items[1].value4"
											class="form-control"></td>

									<td class="text-center" style="width: 10%;">
										<span style="cursor:pointer;" onclick="addBudgetDetailsRow();" tabindex="0"
											id="tempBudgetDetails[0].addButton" data-toggle="tooltip" title=""
											data-original-title="press SPACE to Add!" aria-hidden="true"><i
												class="fa fa-plus"></i></span>
										<span class="add-padding debit-delete-row"
											onclick="deleteBudgetDetailsRow(this);"><i class="fa fa-trash"
												aria-hidden="true" data-toggle="tooltip" title=""
												data-original-title="Delete!"></i></span>
									</td>
								</tr>

								<tr id="closingBalancerow">
									<td style="width: 30%;">Closing Balance as on 31.03.<%= endYear %></td>
									<td style="width: 15%;"><input type="text" name="items[2].value1"
											class="form-control"></td>
									<td style="width: 15%;"><input type="text" name="items[2].value2"
											class="form-control"></td>
									<td style="width: 15%;"><input type="text" name="items[2].value3"
											class="form-control"></td>
									<td style="width: 15%;"><input type="text" name="items[2].value4"
											class="form-control"></td>
									<td style="width: 10%;"></td>
								</tr>
							</tbody>
						</table>

						<!-- Submit Button -->
						<div class="text-center mt-4">
							<button type='submit' class='btn btn-primary' id="buttonSubmit">
								<spring:message code='lbl.create' text="Create" />
							</button>
						</div>

						<div class="col-sm-9 add-margin mb-3">
							<strong class="text-danger">
								<i class="fa fa-star"></i>
								&nbsp; BE: Budget Estimate, RE: Revised Estimate, BE: Budget Estimate.
							</strong>
						</div>

					</div> <!-- /panel-body -->
				</div> <!-- /panel -->
			</div> <!-- /col-md-12 -->
		</div> <!-- /row -->
	</div> <!-- /main-content -->
</form:form>



<!-- JS -->
 <script
  src="<cdn:url value='/resources/app/js/i18n/jquery.i18n.properties.js?rnd=${app_release_no}' context='/services/EGF'/>">
</script>
<script src="<cdn:url value='/resources/app/js/budget/budgetItemFormHelper.js' context='/services/EGF'/>"></script>
<script src="<cdn:url value='/resources/app/js/common/helper.js?rnd=${app_release_no}' context='/services/EGF'/>">
</script>
<!-- <script>
$(document).ready(function () {
    var rowIndex = 2; // continue from existing rows
    var budgetHeadOptions = '<option value="">-- Select --</option>'; // placeholder

    // ✅ Load budget heads from server via AJAX (once)
    $.ajax({
        url: "/services/EGF/budgethead/ajaxBudgetHead",
        type: "GET",
        data: { query: "" }, // send empty query to get all, or change logic if needed
        success: function(data) {
            data.forEach(function(head) {
                budgetHeadOptions += `<option value="${head.id}">${head.name} - ${head.code}</option>`;
            });

            // Populate existing selects once data is ready
            $("#dynamicTable tbody select").each(function() {
                $(this).html(budgetHeadOptions);
            });
        },
        error: function() {
            alert("Failed to load budget heads.");
        }
    });

    // ✅ Add new row
    $(document).on("click", ".addRow", function () {
        let newRow = `<tr>
            <td>
                <select name="items[${rowIndex}].category" class="form-control">
                    ${budgetHeadOptions}
                </select>
            </td>
            <td><input type="text" name="items[${rowIndex}].value1" class="form-control"></td>
            <td><input type="text" name="items[${rowIndex}].value2" class="form-control"></td>
            <td><input type="text" name="items[${rowIndex}].value3" class="form-control"></td>
            <td><input type="text" name="items[${rowIndex}].value4" class="form-control"></td>
            <td class="text-center">
                <span style="cursor:pointer;" class="addRow" tabindex="0"
                    data-toggle="tooltip" title="Add new row" aria-hidden="true">
                    <i class="fa fa-plus text-success"></i>
                </span>
                <span class="add-padding removeRow" style="cursor:pointer;"
                    data-toggle="tooltip" title="Delete row" aria-hidden="true">
                    <i class="fa fa-trash text-danger"></i>
                </span>
            </td>
        </tr>`;

        // Insert new row before closing balance row
        $("#dynamicTable tbody tr:last").before(newRow);

        rowIndex++;
    });

    // ✅ Remove row (ensure at least one dynamic row remains)
    $(document).on("click", ".removeRow", function () {
        let totalDynamicRows = $("#dynamicTable tbody tr").length - 2;

        if (totalDynamicRows > 1) {
            $(this).closest("tr").remove();
        } else {
            alert("At least one row must be present.");
        }
    });

    // ✅ Activate tooltips
    $('[data-toggle="tooltip"]').tooltip();

    // ✅ Validate before submit
    $("#buttonSubmit").click(function (e) {
        if (!$('#budgetItemFunction').valid()) {
            e.preventDefault();
        }
    });
});
</script> -->