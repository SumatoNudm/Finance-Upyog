package org.egov.model.service;

import org.egov.model.budget.FunctionBudgetHead;
import org.egov.model.repository.FunctionBudgetHeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FunctionBudgetHeadService {

    @Autowired
    private FunctionBudgetHeadRepository functionBudgetHeadRepository;


    public List<FunctionBudgetHead> functionBudgetHeads(Long functionId) {
        return functionBudgetHeadRepository.findByFunctionId(functionId);
    }



}
