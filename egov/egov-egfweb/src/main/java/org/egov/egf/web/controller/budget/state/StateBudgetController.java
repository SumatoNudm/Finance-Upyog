package org.egov.egf.web.controller.budget.state;


import org.apache.log4j.Logger;
import org.egov.egf.statefinance.model.BudgetRegisterDTO;
import org.egov.egf.statefinance.model.BudgetRegisterWrapper;
import org.egov.egf.web.controller.microservice.VoucherController;
import org.egov.eis.contract.ResponseInfo;
import org.egov.infra.microservice.utils.MicroserviceUtils;
import org.egov.model.budget.BudgetItem;
import org.egov.model.budget.BudgetRegister;
import org.egov.model.budget.entry.BudgetEntryDTO;
import org.egov.model.budget.register.BudgetRegisterFullRequestDTO;
import org.egov.model.budget.register.BudgetRegisterFullResponseDTO;
import org.egov.model.service.BudgetItemService;
import org.egov.model.service.BudgetRegisterWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

@RestController
public class StateBudgetController {

    private static final Logger LOGGER = Logger.getLogger(StateBudgetController.class);

    @Autowired
    private BudgetRegisterWorkflowService budgetRegisterWorkflowService;

    @Autowired
    private BudgetItemService budgetItemService;

    @Autowired
    private MicroserviceUtils microserviceUtils;


    @PostMapping(value = "/rest/budget/test")
    @ResponseBody
    private Map<String, Object> testApi() {
        LOGGER.info("mridx: inside test");
        Map<String, Object> response = new HashMap<>();
        response.put("testing", "OK");

        return response;
    }


    @PostMapping(value = "/rest/budget/getfullbudget")
    @ResponseBody
    public ResponseEntity<BudgetRegisterFullResponseDTO> getFullBudget(@RequestBody @Valid BudgetRegisterFullRequestDTO budgetRegisterFullRequestDTO) {

        LOGGER.info("mridx: inside get full budget");

        ResponseInfo responseInfo = new ResponseInfo();
        BudgetRegisterFullResponseDTO responseDTO = new BudgetRegisterFullResponseDTO();

        BudgetRegister budgetRegister = budgetRegisterWorkflowService.findBudgetRegisterByRegisterNumber(budgetRegisterFullRequestDTO.budgetRegisterNumber);

        if (budgetRegister == null) {
           responseInfo.setStatus("failed");
           responseDTO.setResponseInfo(responseInfo);
           responseDTO.setMessage("Budget register invalid.");
           return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                   .body(responseDTO);
        }

        final List<BudgetItem> budgetItems = budgetItemService.getAllBudgetItemsByBudgetRegister(budgetRegister);

        ArrayList<BudgetEntryDTO> budgetEntryDTOs = new ArrayList<>();


        for (BudgetItem budgetItem : budgetItems) {
            LOGGER.info(budgetItem.toString());

            BudgetEntryDTO budgetEntryDTO = new BudgetEntryDTO();

            budgetEntryDTO.setId(budgetItem.getId());

            budgetEntryDTO.setFunctionId(budgetItem.getFunction().getId());
            budgetEntryDTO.setFunctionName(budgetItem.getFunction().getName());
            budgetEntryDTO.setFunctionCode(budgetItem.getFunction().getCode());

            if (null != budgetItem.getBudgetHead()) {
                budgetEntryDTO.setBudgetHeadId(budgetItem.getBudgetHead().getId());
                budgetEntryDTO.setBudgetHeadName(budgetItem.getBudgetHead().getName());
                budgetEntryDTO.setBudgetHeadCode(budgetItem.getBudgetHead().getCode());
                budgetEntryDTO.setBudgetHeadAccountType(budgetItem.getBudgetHead().getAccountType().name());
                budgetEntryDTO.setBudgetHeadAccountTypeCode(budgetItem.getBudgetHead().getAccountTypeCode());
                budgetEntryDTO.setBudgetHeadCategory(budgetItem.getBudgetHead().getCategory());
                budgetEntryDTO.setBudgetHeadStateCode(budgetItem.getBudgetHead().getStateCode());
            }

            budgetEntryDTO.setBudgetCode(budgetItem.getBudgetCode());

            budgetEntryDTO.setCurrentEstimate(budgetItem.getCurrentEstimate());
            budgetEntryDTO.setCurrentActual(budgetItem.getCurrentActual());
            budgetEntryDTO.setCurrentRevisedEstimate(budgetItem.getCurrentRevisedEstimate());
            budgetEntryDTO.setNextEstimate(budgetItem.getNextEstimate());

            if (null != budgetItem.getScheme()) {
                budgetEntryDTO.setSchemeId(budgetItem.getScheme().getId());
                budgetEntryDTO.setSchemeCode(budgetItem.getScheme().getCode());
                budgetEntryDTO.setSchemeName(budgetItem.getScheme().getName());
            }

            budgetEntryDTO.setStateBudgetCode(budgetItem.getStateBudgetCode());

            budgetEntryDTOs.add(budgetEntryDTO);

        }

        BudgetRegisterWrapper budgetRegisterWrapper = BudgetRegisterWrapper.fromBudgetRegister(budgetRegister, microserviceUtils.getTenentId(), microserviceUtils.getHeaderNameForTenant());

//        BudgetRegisterFullResponseDTO responseDTO = new BudgetRegisterFullResponseDTO(budgetRegisterWrapper, budgetEntryDTOs);

        responseInfo.setStatus("success");

        responseDTO.setResponseInfo(responseInfo);
        responseDTO.setBudgetRegisterWrapper(budgetRegisterWrapper);
        responseDTO.setBudgetEntries(budgetEntryDTOs);

        responseDTO.setMessage("Budget Retrieved !");



        return ResponseEntity.ok(responseDTO);


    }


}
