package org.egov.model.service;

import java.util.List;

import org.egov.commons.CFunction;
import org.egov.commons.repository.FunctionRepository;
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
//            if (form.getClosing() != null) {
//                BudgetItem closing = form.getClosing();
//                closing.setBudgetGroup("Closing_Balance");
//                budgetItemRepository.save(closing);
//            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    
}
