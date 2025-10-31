<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
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
							<spring:message code="lbl.budget.input" text="Budget Input" />
						</div>
					</div>

					<div class="panel-body">
						<!-- Function Info -->
						<div class="col-sm-9 add-margin mb-6">
							<strong>Function:</strong>
							${function.name} (<span class="text-muted">${function.code}</span>)
						</div>

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
									<td style="width: 15%;"><input type="text" name="items[0].value1" class="form-control"></td>
									<td style="width: 15%;"><input type="text" name="items[0].value2" class="form-control"></td>
									<td style="width: 15%;"><input type="text" name="items[0].value3" class="form-control"></td>
									<td style="width: 15%;"><input type="text" name="items[0].value4" class="form-control"></td>
									<td style="width: 10%;"></td>
								</tr>

								<tr>
									<td style="width: 30%;">
										<select name="items[1].category" class="form-control">
											<option value="">-- Select --</option>
											<option value="TYPE1">Type 1</option>
											<option value="TYPE2">Type 2</option>
											<option value="TYPE3">Type 3</option>
										</select>
									</td>
									<td style="width: 15%;"><input type="text" name="items[1].value1" class="form-control"></td>
									<td style="width: 15%;"><input type="text" name="items[1].value2" class="form-control"></td>
									<td style="width: 15%;"><input type="text" name="items[1].value3" class="form-control"></td>
									<td style="width: 15%;"><input type="text" name="items[1].value4" class="form-control"></td>
									
									<td class="text-center"  style="width: 10%;">
										<span style="cursor:pointer;" class="addRow" tabindex="0" data-toggle="tooltip"
											title="Press SPACE to Add!" aria-hidden="true">
											<i class="fa fa-plus text-success"></i>
										</span>
										<span class="add-padding removeRow" style="cursor:pointer;"
											data-toggle="tooltip" title="Delete!" aria-hidden="true">
											<i class="fa fa-trash text-danger"></i>
										</span>
									</td>
								</tr>

								<tr>
									<td style="width: 30%;">Closing Balance as on 31.03.<%= endYear %></td>
									<td style="width: 15%;"><input type="text" name="items[2].value1" class="form-control"></td>
									<td style="width: 15%;"><input type="text" name="items[2].value2" class="form-control"></td>
									<td style="width: 15%;"><input type="text" name="items[2].value3" class="form-control"></td>
									<td style="width: 15%;"><input type="text" name="items[2].value4" class="form-control"></td>
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
<script>
	$(document).ready(function () {
		var rowIndex = 2; // continue from existing rows

		// Add new row
		$(document).on("click", ".addRow", function () {
			let newRow = `<tr>
                <td>
                    <select name="items[${rowIndex}].category" class="form-control">
                        <option value="">-- Select --</option>
                        <option value="TYPE1">Type 1</option>
                        <option value="TYPE2">Type 2</option>
                        <option value="TYPE3">Type 3</option>
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

			// ✅ Insert new row before the closing balance row
			$("#dynamicTable tbody tr:last").before(newRow);

			rowIndex++;
		});

		// Remove row
		$(document).on("click", ".removeRow", function () {
			// Count total editable rows (exclude first and last fixed rows)
			let totalDynamicRows = $("#dynamicTable tbody tr").length - 2;

			if (totalDynamicRows > 1) {
				$(this).closest("tr").remove();
			} else {
				alert("At least one row must be present.");
			}
		});

		// Bootstrap tooltip activation
		$('[data-toggle="tooltip"]').tooltip();

		// Validate before submit
		$("#buttonSubmit").click(function (e) {
			if (!$('#budgetItemFunction').valid()) {
				e.preventDefault();
			}
		});
	});
</script>

