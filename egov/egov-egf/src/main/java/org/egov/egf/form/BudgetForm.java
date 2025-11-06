package org.egov.egf.form;

import org.egov.model.budget.BudgetItem;

public class BudgetForm {
    
    private Long functionid;

    private BudgetItem opening;

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
}
