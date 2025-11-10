package org.egov.model.repository;

import org.egov.model.budget.FunctionBudgetHead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FunctionBudgetHeadRepository extends JpaRepository<FunctionBudgetHead, Long> {

    List<FunctionBudgetHead> findByFunctionId(Long functionId);

}
