package org.egov.egf.statefinance.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.egov.infra.microservice.models.Payment;
import org.egov.infra.microservice.models.ResponseInfo;

import java.util.List;
import java.util.Map;

public class BudgetRegisterResponse {

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    @JsonProperty("Data")
    private BudgetRegisterDTO data;



}


