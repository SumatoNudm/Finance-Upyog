package org.egov.egf.statefinance.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.egov.infra.microservice.models.RequestInfo;

@Getter
@Setter
public class BudgetRegisterRequestWrapper {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("data")
    private BudgetRegisterWrapper budgetRegister;

    @JsonProperty("tenantId")
    private String tenantId;

}
