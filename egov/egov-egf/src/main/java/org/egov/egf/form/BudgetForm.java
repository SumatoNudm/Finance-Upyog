package org.egov.egf.form;

import java.util.ArrayList;
import java.util.List;
import org.egov.model.budget.BudgetItem;

public class BudgetForm {

    private Long functionid;             // Selected Function ID

    private BudgetItem opening;          // Opening Balance row

    private List<BudgetItem> items;      // Revenue/Capital rows (multiple)

    private BudgetItem closing;          // Closing Balance row

    public BudgetForm() {
        // Ensure the list is never null
        this.items = new ArrayList<>();
    }

    // ===== Getters and Setters =====

    public Long getFunctionid() {
        return functionid;
    }

    public void setFunctionid(Long functionid) {
        this.functionid = functionid;
    }

    public BudgetItem getOpening() {
        return opening;
    }

    public void setOpening(BudgetItem opening) {
        this.opening = opening;
    }

    public List<BudgetItem> getItems() {
        return items;
    }

    public void setItems(List<BudgetItem> items) {
        this.items = items;
    }

    public BudgetItem getClosing() {
        return closing;
    }

    public void setClosing(BudgetItem closing) {
        this.closing = closing;
    }
}
