<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<form:form role="form" action="create" modelAttribute="function" id="budgetItemFunction"
	cssClass="form-horizontal form-groups-bordered" enctype="multipart/form-data">
	<div class="form-group">
		<div class="text-center">

			<div class="main-content">
				<div class="row">
					<div class="col-md-12">
						<div class="panel panel-primary" data-collapsed="0">
							<div class="panel-heading">
								<div class="panel-title">
									<spring:message code="lbl.budget.input" text="Budget Input" />
								</div>
							</div>

							<div class="col-sm-3 add-margin">

								<p>${function.code}</p>
								<p>${function.name}</p>


							</div>

							<table class="table table-bordered" id="dynamicTable">
								<thead>
								<tr>
									<th>Category</th>
									<th>Value 1</th>
									<th>Value 2</th>
									<th>Value 3</th>
									<th>Value 4</th>
									<th>Action</th>
								</tr>
								</thead>

								<tbody>
								<tr>
									<td>
										<select name="items[0].category" class="form-control">
											<option value="">-- Select --</option>
											<option value="TYPE1">Type 1</option>
											<option value="TYPE2">Type 2</option>
											<option value="TYPE3">Type 3</option>
										</select>
									</td>

									<td><input type="text" name="items[0].value1" class="form-control"></td>
									<td><input type="text" name="items[0].value2" class="form-control"></td>
									<td><input type="text" name="items[0].value3" class="form-control"></td>
									<td><input type="text" name="items[0].value4" class="form-control"></td>

									<td><button type="button" class="btn btn-danger removeRow">X</button></td>
								</tr>
								</tbody>
							</table>

							<button type="button" id="addRow" class="btn btn-primary">+ Add Row</button>


						</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	<button type='submit' class='btn btn-primary' id="buttonSubmit">
		<spring:message code='lbl.create' text="Create" />
	</button>

	</div>
</form:form>
<script>
	$('#buttonSubmit').click(function (e) {
		if ($('form').valid()) {} else {
			e.preventDefault();
		}
	});

	$(document).ready(function(){

    var rowIndex = 1; // start indexing from next row

    $("#addRow").click(function () {

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

            <td><button type="button" class="btn btn-danger removeRow">X</button></td>
        </tr>`;

        $("#dynamicTable tbody").append(newRow);
        rowIndex++;
    });

    // Remove row
    $(document).on("click", ".removeRow", function () {
        $(this).closest("tr").remove();
    });

});

</script>