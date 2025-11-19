package org.egov.egf.form;

import java.util.ArrayList;
import java.util.List;
import org.egov.model.budget.BudgetItem;

public class BudgetForm {

    private Long functionid;             // Selected Function ID

    private BudgetItem opening;          // Opening Balance row

    private List<BudgetItem> items;      // Revenue/Capital rows (multiple)

    private Long financialYear;

    private Long currentFinancialYear;

    private Long stateBudgetCode;

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


    public Long getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(Long financialYear) {
        this.financialYear = financialYear;
    }

    public Long getCurrentFinancialYear() {
        return currentFinancialYear;
    }

    public void setCurrentFinancialYear(Long currentFinancialYear) {
        this.currentFinancialYear = currentFinancialYear;
    }

    public Long getStateBudgetCode() {
        return stateBudgetCode;
    }

    public void setStateBudgetCode(Long stateBudgetCode) {
        this.stateBudgetCode = stateBudgetCode;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Function Id: ").append(functionid);
//        stringBuilder.append("\n Opening Balance: ").append(opening.toString());
//        stringBuilder.append("\n Closing Balance: ").append(closing.toString());
        stringBuilder.append("\n\n");

//        items.forEach(budgetItem -> {
//            stringBuilder.append("\n ").append(budgetItem.toString());
//        });

        return stringBuilder.toString();

    }

}
