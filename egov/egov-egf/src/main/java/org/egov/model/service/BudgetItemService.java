package org.egov.model.service;

import org.egov.egf.form.BudgetForm;
import org.egov.model.budget.BudgetItem;
import org.egov.model.repository.BudgetItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetItemService {

    private final BudgetItemRepository budgetItemRepository;

    @Autowired
    public BudgetItemService(final BudgetItemRepository budgetItemRepository) {
        this.budgetItemRepository = budgetItemRepository;
    }

    public BudgetItem create(BudgetItem item) {
        return budgetItemRepository.save(item);
    }

    public void saveAll(BudgetForm form) {
        // Save Opening Balance
        if (form.getOpening() != null) {
            BudgetItem opening = form.getOpening();
            opening.setBudgetGroup("Opening_Balance");
            budgetItemRepository.save(opening);
        }

        // Save Revenue/Capital Budget Items
        // if (form.getItems() != null && !form.getItems().isEmpty()) {
        //     List<BudgetItem> items = form.getItems();
        //     for (BudgetItem item : items) {
        //         // item.getBudgetGroup() could be "Revenue_Budget" or "Capital_Budget"
        //         budgetItemRepository.save(item);
        //     }
        // }

        // Save Closing Balance
        // if (form.getClosing() != null) {
        //     BudgetItem closing = form.getClosing();
        //     closing.setBudgetGroup("Closing_Balance");
        //     budgetItemRepository.save(closing);
        // }
    }
    
}
