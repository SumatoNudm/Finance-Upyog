package org.egov.model.budget.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.egf.statefinance.model.BudgetRegisterDTO;
import org.egov.egf.statefinance.model.BudgetRegisterWrapper;
import org.egov.eis.contract.ResponseInfo;
import org.egov.model.budget.BudgetItem;
import org.egov.model.budget.entry.BudgetEntryDTO;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BudgetRegisterFullResponseDTO {

    @JsonProperty("ResponseInfo")
    ResponseInfo responseInfo;

    @JsonProperty("message")
    String message;

    @JsonProperty("budgetRegister")
    BudgetRegisterWrapper budgetRegisterWrapper;

    @JsonProperty("budgetEntries")
    List<BudgetEntryDTO> budgetEntries;


}
