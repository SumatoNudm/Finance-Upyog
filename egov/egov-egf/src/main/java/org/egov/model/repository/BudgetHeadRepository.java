package org.egov.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.egov.model.budget.BudgetHead;

@Repository
public interface BudgetHeadRepository extends JpaRepository<BudgetHead, Long> {
    
    // List<BudgetHead> findByAccountTypeIs(String accountType);

    // List<BudgetHead> findByIsActiveTrue();



}
