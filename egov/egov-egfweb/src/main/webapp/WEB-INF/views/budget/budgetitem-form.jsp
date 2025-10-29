<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
	<input type="hidden" value="${mode }" id="mode" xmlns:spring="http://www.w3.org/1999/XSL/Transform" />
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
						<div class="form-group">
							<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.function" text="Function" /> <span class="mandatory"></span>
							</label>
							<div class="col-sm-3 add-margin">
								<c:if test="${budgetItem.function != null}">
									<form:input path="" name="function" id="function" class="form-control"
										placeholder="Type first 3 letters of Function name" required="required"
										value="${budgetItem.function.code} - ${budgetItem.function.name}" />
								</c:if>
								<c:if test="${budgetItem.function == null}">
									<form:input path="" name="function" id="function" class="form-control"
										placeholder="Type first 3 letters of Function name" required="required" />
								</c:if>
								<form:hidden path="budgetItem.function" name="budgetItem.function"
									id="budgetItem.function" class="form-control table-input hidden-input cfunction" />
								<form:errors path="budgetItem.function" cssClass="add-margin error-msg" />
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.budget.head" text="Budget Head" /> <span
									class="mandatory"></span>
							</label>
							<div class="col-sm-6 add-margin">
								<form:input path="budgetHeadId" class="form-control text-left patternvalidation"
									data-pattern="alphanumeric" maxlength="250" required="required" />
								<form:errors path="budgetHeadId" cssClass="error-msg" />
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.code" text="Budget Group" /> <span class="mandatory"></span>
							</label>
							<div class="col-sm-6 add-margin">
								<form:input path="budgetGroup" class="form-control text-left patternvalidation"
									data-pattern="alphanumeric" maxlength="250" required="required" />
								<form:errors path="budgetGroup" cssClass="error-msg" />
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.name" text="Financial Year" /> <span class="mandatory"></span>
							</label>
							<div class="col-sm-6 add-margin">
								<form:input path="financialYear" class="form-control text-left patternvalidation"
									data-pattern="alphanumeric" maxlength="250" required="required" />
								<form:errors path="financialYear" cssClass="error-msg" />
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.name" text="Current Financial Year" /> <span
									class="mandatory"></span>
							</label>
							<div class="col-sm-6 add-margin">
								<form:input path="currentFinancialYear" class="form-control text-left patternvalidation"
									data-pattern="alphanumeric" maxlength="250" required="required" />
								<form:errors path="currentFinancialYear" cssClass="error-msg" />
							</div>
						</div>



						<div class="form-group">
							<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.name" text="Current Estimate" /> <span
									class="mandatory"></span>
							</label>
							<div class="col-sm-6 add-margin">
								<form:input path="currentEstimate" class="form-control text-left patternvalidation"
									data-pattern="alphanumeric" maxlength="250" required="required" />
								<form:errors path="currentEstimate" cssClass="error-msg" />
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.name" text="Current Actual" /> <span class="mandatory"></span>
							</label>
							<div class="col-sm-6 add-margin">
								<form:input path="currentActual" class="form-control text-left patternvalidation"
									data-pattern="alphanumeric" maxlength="250" required="required" />
								<form:errors path="currentActual" cssClass="error-msg" />
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.name" text="Current Revised Estimate" /> <span
									class="mandatory"></span>
							</label>
							<div class="col-sm-6 add-margin">
								<form:input path="currentRevisedEstimate"
									class="form-control text-left patternvalidation" data-pattern="alphanumeric"
									maxlength="250" required="required" />
								<form:errors path="currentRevisedEstimate" cssClass="error-msg" />
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label text-right">
								<spring:message code="lbl.name" text="Next Estimate" /> <span class="mandatory"></span>
							</label>
							<div class="col-sm-6 add-margin">
								<form:input path="nextEstimate" class="form-control text-left patternvalidation"
									data-pattern="alphanumeric" maxlength="250" required="required" />
								<form:errors path="nextEstimate" cssClass="error-msg" />
							</div>
						</div>

						<input type="hidden" name="budgetItem" value="${budgetItem.id}" />
					</div>
				</div>
			</div>
		</div>
	</div>