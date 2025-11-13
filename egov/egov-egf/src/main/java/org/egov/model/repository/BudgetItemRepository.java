package org.egov.model.repository;


import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.model.budget.BudgetItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetItemRepository extends JpaRepository<BudgetItem, Long> {



//    List<BudgetItem> findByBudgetGroupAndCurrentFinancialYearIdAndFunction(
//            String budgetGroup,
//            Long currentFinancialYearId,
//            CFunction function
//    );


    List<BudgetItem> findByBudgetGroupInAndFunctionAndCurrentFinancialYear(
            List<String> budgetGroup, CFunction function, CFinancialYear currentFinancialYear);


}
