package org.egov.model.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.repository.FunctionRepository;
import org.egov.commons.service.CFinancialYearService;
import org.egov.egf.form.BudgetForm;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.model.budget.BudgetItem;
import org.egov.model.repository.BudgetItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BudgetItemService {

    private final BudgetItemRepository budgetItemRepository;

    private final FunctionRepository functionRepository;

    private final static Logger LOGGER = Logger.getLogger(BudgetItemService.class);

    @Autowired
	private CFinancialYearService financialYearService;

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

            LOGGER.info("FY" + form.getFinancialYear() + "CFY" + form.getCurrentFinancialYear());

            CFinancialYear financialYear = financialYearService.findOne(form.getFinancialYear());

            CFinancialYear nextFinancialYear = financialYearService.findOne(form.getCurrentFinancialYear());

            if (financialYear == null || nextFinancialYear == null) {
                throw new Exception("Financial year not found !");
            }

            // Save Opening Balance
            if (form.getOpening() != null) {
                BudgetItem opening = form.getOpening();
                opening.setBudgetGroup("Opening_Balance");
                opening.setFunction(function);
                opening.setFinancialYear(financialYear.getId());
                opening.setCurrentFinancialYear(nextFinancialYear.getId());
                budgetItemRepository.save(opening);
            }

            // Save Revenue/Capital Budget Items
            if (form.getItems() != null && !form.getItems().isEmpty()) {
                List<BudgetItem> items = form.getItems();
                for (BudgetItem item : items) {
                    item.setFunction(function);
                    item.setFinancialYear(financialYear.getId());
                    item.setCurrentFinancialYear(nextFinancialYear.getId());
                    budgetItemRepository.save(item);
                }
            }

            // Save Closing Balance
            if (form.getClosing() != null) {
                BudgetItem closing = form.getClosing();
                closing.setBudgetGroup("Closing_Balance");
                closing.setFunction(function);
                closing.setFinancialYear(financialYear.getId());
                closing.setCurrentFinancialYear(nextFinancialYear.getId());
                budgetItemRepository.save(closing);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    
}
