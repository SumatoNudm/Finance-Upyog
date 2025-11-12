package org.egov.model.service;

import java.util.List;

import org.egov.commons.CFunction;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.repository.FunctionRepository;
import org.egov.commons.service.CFinancialYearService;
import org.egov.egf.form.BudgetForm;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.model.budget.BudgetItem;
import org.egov.model.budget.BudgetRegister;
import org.egov.model.repository.BudgetItemRepository;
import org.egov.utils.FinancialConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.egov.utils.FinancialConstants.BUDGET_MODULE;

@Service
public class BudgetItemService {

    private final static Logger LOGGER = LoggerFactory.getLogger(BudgetItemService.class);

    private final BudgetItemRepository budgetItemRepository;

    private final FunctionRepository functionRepository;

    @Autowired
    private BudgetRegisterWorkflowService budgetRegisterWorkflowService;


    @Autowired
    private CFinancialYearService financialYearService;

    @Autowired
    private EgwStatusHibernateDAO egwStatusDAO;

    @Autowired
    public BudgetItemService(final BudgetItemRepository budgetItemRepository, final FunctionRepository functionRepository) {
        this.budgetItemRepository = budgetItemRepository;
        this.functionRepository = functionRepository;
    }

    public BudgetItem create(BudgetItem item) {
        return budgetItemRepository.save(item);
    }

    @Transactional
    public void saveBudgetInputForm(BudgetForm form) {

        try {
            // validate function

            CFunction function = functionRepository.findOne(form.getFunctionid());

            if (function == null) {
                throw new Exception("The selected function not found !");
            }

            // Save Opening Balance
            if (form.getOpening() != null) {
                BudgetItem opening = form.getOpening();
                opening.setBudgetGroup("Opening_Balance");
                opening.setFunction(function);
                budgetItemRepository.save(opening);
            }

            // Save Revenue/Capital Budget Items
            if (form.getItems() != null && !form.getItems().isEmpty()) {
                List<BudgetItem> items = form.getItems();
                for (BudgetItem item : items) {
                    //inject function id
                    item.setFunction(function);
                    budgetItemRepository.save(item);
                }
            }




            // Save Closing Balance
            if (form.getClosing() != null) {
                BudgetItem closing = form.getClosing();
                closing.setBudgetGroup("Closing_Balance");
                closing.setFunction(function);
                budgetItemRepository.save(closing);
            }


            final BudgetRegister budgetRegister = new BudgetRegister();
            budgetRegister.setBudgetRegisterNumber("bud-2026-27-001");
            budgetRegister.setFinancialYear(financialYearService.getCurrentFinancialYear());
            budgetRegister.setBudgetType("RE");
//            budgetRegister.setStatus(egwStatusDAO.getStatusByModuleAndCode(FinancialConstants.BUDGET_MODULE, FinancialConstants.BUDGET_CREATED_STATUS));


           BudgetRegister saved =  budgetRegisterWorkflowService.create(
                    budgetRegister,101L, "Initial submission for review", null, "START", "FMO"
            );


           LOGGER.info("Budget Register");
            LOGGER.info("ID:{}", saved.getId());
            LOGGER.info("Number:{}", saved.getBudgetRegisterNumber());
            LOGGER.info("Workflow State:{}", saved.getCurrentState().getValue());



        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    
}
