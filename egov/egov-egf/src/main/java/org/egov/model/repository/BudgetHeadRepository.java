package org.egov.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.egov.model.budget.BudgetHead;

@Repository
public interface BudgetHeadRepository extends JpaRepository<BudgetHead, Long> {
    
    // List<BudgetHead> findByAccountTypeIs(String accountType);

    // List<BudgetHead> findByIsActiveTrue();


    List<BudgetHead> findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(String code, String name);


    @Query(value = "SELECT * FROM " + BudgetHead.TABLE_NAME  + " bh " +
            "WHERE FIND_IN_SET(:functionId, bh.function_ids) > 0 " +
            "AND (LOWER(bh.code) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "     OR LOWER(bh.name) LIKE LOWER(CONCAT('%', :query, '%')))",
            nativeQuery = true)
    List<BudgetHead> searchByFunctionAndQuery(@Param("functionId") Long functionId, @Param("query") String query);

}
