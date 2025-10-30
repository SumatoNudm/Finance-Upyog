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
</script>