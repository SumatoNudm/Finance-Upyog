package org.egov.model.budget.register;


import org.codehaus.jackson.annotate.JsonProperty;
import org.egov.egf.statefinance.model.BudgetRegisterDTO;
import org.egov.infra.microservice.contract.RequestInfoWrapper;

import javax.validation.constraints.NotNull;



public class BudgetRegisterActionsDTO extends RequestInfoWrapper {

    @NotNull
    public BudgetRegisterAction action;

    public enum BudgetRegisterAction {
        APPROVE,
        REJECT
    }

    public String remarks;

    public String tenantId;

    @JsonProperty("budgetregister")
    public BudgetRegisterDTO budgetRegister;


}
