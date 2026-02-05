package org.egov.egf.web.controller.budget;


import org.apache.log4j.Logger;
import org.egov.egf.statefinance.model.BudgetRegisterRequestWrapper;
import org.egov.egf.statefinance.model.BudgetRegisterWrapper;
import org.egov.egf.web.controller.microservice.VoucherController;
import org.egov.model.budget.register.BudgetRegisterActionsDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class StateFinanceController {

    private static final Logger LOGGER = Logger.getLogger(StateFinanceController.class);

    @PostMapping(value = "/rest/budget/stateaction")
    @ResponseBody
    public Map<String, Object> handleStateAction(@RequestBody BudgetRegisterActionsDTO budgetRegisterActionsDTO) {
        Map<String, Object> response = new HashMap<>();
        response.put("action", "action");
        response.put("data", budgetRegisterActionsDTO);
        return response;
    }


}
