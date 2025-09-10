package org.egov.model.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.model.budget.BudgetHead;
import org.egov.model.repository.BudgetHeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BudgetHeadService {

    private final BudgetHeadRepository budgetHeadRepository;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource messageSource;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public BudgetHeadService(final BudgetHeadRepository budgetHeadRepository) {
        this.budgetHeadRepository = budgetHeadRepository;
    }

    // @Transactional
    // public BudgetHead create(final BudgetHead budgetHead) {
    // return budgetHeadRepository.save(budgetHead);
    // }
    @Transactional
    public BudgetHead create(final BudgetHead budgetHead) {
        if (budgetHead.getAccountType() != null) {
            switch (budgetHead.getAccountType()) {
                case REVENUE_RECEIPTS:
                    budgetHead.setAccountTypeCode("RR");
                    break;
                case REVENUE_EXPENDITURE:
                    budgetHead.setAccountTypeCode("RE");
                    break;
                case CAPITAL_RECEIPTS:
                    budgetHead.setAccountTypeCode("CR");
                    break;
                case CAPITAL_EXPENDITURE:
                    budgetHead.setAccountTypeCode("CE");
                    break;
                default:
                    budgetHead.setAccountTypeCode(null); // or throw exception
            }
        }
        return budgetHeadRepository.save(budgetHead);
    }

}
