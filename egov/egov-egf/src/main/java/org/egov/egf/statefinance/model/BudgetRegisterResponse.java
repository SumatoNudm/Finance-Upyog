package org.egov.egf.statefinance.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.infra.microservice.models.Payment;
import org.egov.infra.microservice.models.ResponseInfo;
import org.python.antlr.ast.Str;

import java.util.List;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetRegisterResponse {

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;


}


