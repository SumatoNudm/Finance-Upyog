package org.egov.model.repository;


import org.egov.model.budget.BudgetItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetItemRepository extends JpaRepository<BudgetItem, Long> {


    List<BudgetItem> findByBudgetRegisterId(Long registerId);


//    List<BudgetItem> findByBudgetGroupAndCurrentFinancialYearIdAndFunction(
//            String budgetGroup,
//            Long currentFinancialYearId,
//            CFunction function
//    );


    List<BudgetItem> findByBudgetGroupInAndFunctionAndCurrentFinancialYear(
            List<String> budgetGroup, CFunction function, CFinancialYear currentFinancialYear);


    List<BudgetItem> findByFunctionAndCurrentFinancialYear(CFunction function, CFinancialYear currentFinancialYear);


    @Query("select count(b) > 0 from BudgetItem b " +
            "where b.function.id = :functionId " +
            "and b.currentFinancialYear.id = :currentFinancialYearId")
    boolean existsBudgetForCurrentFY(@Param("functionId") Long functionId,
                                     @Param("currentFinancialYearId") Long currentFinancialYearId);


}
