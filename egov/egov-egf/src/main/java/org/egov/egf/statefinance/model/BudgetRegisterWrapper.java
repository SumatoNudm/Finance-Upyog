package org.egov.egf.statefinance.model;

import lombok.Getter;
import lombok.Setter;
import org.egov.model.budget.BudgetRegister;

@Getter
@Setter
public class BudgetRegisterWrapper {

    public static BudgetRegisterWrapper fromBudgetRegister(BudgetRegister budgetRegister, String tenantId, String cityName) {
        BudgetRegisterWrapper budgetRegisterWrapper = new BudgetRegisterWrapper();
        budgetRegisterWrapper.setTenantId(tenantId);
        budgetRegisterWrapper.setCityName(cityName);
        budgetRegisterWrapper.setBudgetRegisterId(budgetRegister.getId());
        budgetRegisterWrapper.setBudgetRegisterNumber(budgetRegister.getBudgetRegisterNumber());
        budgetRegisterWrapper.setBudgetRegisterName(budgetRegister.getBudgetRegisterName());
        budgetRegisterWrapper.setCurrentFy(budgetRegister.getCurrentFinancialYear().getFinYearRange());
        budgetRegisterWrapper.setNextFy(budgetRegister.getFinancialYear().getFinYearRange());
        budgetRegisterWrapper.setStartingDate(budgetRegister.getCurrentFinancialYear().getStartingDate().toString());
        budgetRegisterWrapper.setEndingDate(budgetRegister.getCurrentFinancialYear().getEndingDate().toString());
        return budgetRegisterWrapper;
    }


    private String tenantId;

    private String cityName;

    private Long budgetRegisterId;

    private String budgetRegisterNumber;

    private String budgetRegisterName;

    private String currentFy;

    private String nextFy;

    private String startingDate;

    private String endingDate;





}
