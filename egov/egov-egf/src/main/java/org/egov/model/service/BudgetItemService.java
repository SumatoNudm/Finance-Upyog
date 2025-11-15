package org.egov.model.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.repository.FunctionRepository;
import org.egov.commons.service.CFinancialYearService;
import org.egov.egf.form.BudgetForm;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.model.budget.BudgetHead;
import org.egov.model.budget.BudgetItem;
import org.egov.model.repository.BudgetItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class BudgetItemService {

    private final BudgetItemRepository budgetItemRepository;

    private final FunctionRepository functionRepository;

    private final static Logger LOGGER = Logger.getLogger(BudgetItemService.class);

    @Autowired
	private CFinancialYearService financialYearService;

    @Autowired
    private BudgetHeadService budgetHeadService;

    @Autowired
    public BudgetItemService(final BudgetItemRepository budgetItemRepository, final FunctionRepository functionRepository) {
        this.budgetItemRepository = budgetItemRepository;
        this.functionRepository = functionRepository;
    }

    public BudgetItem create(BudgetItem item) {
        return budgetItemRepository.save(item);
    }

    @Transactional
    public void saveBudgetInputForm(BudgetForm form) {

        try {
            // validate function

            CFunction function = functionRepository.findOne(form.getFunctionid());
            
            if (function == null) {
                throw new Exception("The selected function not found !");
            }

            LOGGER.info("FY" + form.getFinancialYear() + "CFY" + form.getCurrentFinancialYear());

            CFinancialYear financialYear = financialYearService.findOne(form.getFinancialYear());

            CFinancialYear nextFinancialYear = financialYearService.findOne(form.getCurrentFinancialYear());

            if (financialYear == null || nextFinancialYear == null) {
                throw new Exception("Financial year not found !");
            }

            // Save Opening Balance
            if (form.getOpening() != null) {
                BudgetItem opening = form.getOpening();
                opening.setBudgetGroup("Opening_Balance");
                opening.setFunction(function);
                opening.setFinancialYear(financialYear);
                opening.setCurrentFinancialYear(nextFinancialYear);
                budgetItemRepository.save(opening);
            }

            // Save Revenue/Capital Budget Items
            if (form.getItems() != null && !form.getItems().isEmpty()) {
                List<BudgetItem> items = form.getItems();
                for (BudgetItem item : items) {
                    item.setFunction(function);
                    item.setFinancialYear(financialYear);
                    item.setCurrentFinancialYear(nextFinancialYear);
                    BudgetHead bh= budgetHeadService.findById(item.getBudgetHead().getId());
                    if (bh == null) {
                        throw new Exception("Invalid budget head on " + item.getBudgetGroup());
                    }
                    item.setBudgetHead(bh);
                    budgetItemRepository.save(item);
                }
            }

            // Save Closing Balance
            if (form.getClosing() != null) {
                BudgetItem closing = form.getClosing();
                closing.setBudgetGroup("Closing_Balance");
                closing.setFunction(function);
                closing.setFinancialYear(financialYear);
                closing.setCurrentFinancialYear(nextFinancialYear);
                budgetItemRepository.save(closing);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void getFunctionWiseBudgetItems(final Long functionId, final Model model) {

    }

//    public List<BudgetItem> findByTypeAndFunctionIdAndFinancialYearId(String type, CFunction function, Long fyId) {
//       return budgetItemRepository.findByBudgetGroupAndCurrentFinancialYearIdAndFunction(type, fyId, function);
//
//    }

    public Map<String, List<BudgetItem>> getBudgetItemsByTypesFunctionFy(
            List<String> types, CFunction function, CFinancialYear financialYear) {

        List<BudgetItem> items = budgetItemRepository
                .findByBudgetGroupInAndFunctionAndCurrentFinancialYear(types, function, financialYear);


//        LOGGER.info("inside service!");
//        LOGGER.info(items.size());
//        items.forEach(i -> LOGGER.info(i.getBudgetCode()));

        return items.stream()
                .collect(Collectors.groupingBy(BudgetItem::getBudgetGroup));
    }

    public List<BudgetItem> getBudgetItemsByFunctionAndCurrentFinancialYear(CFunction function, CFinancialYear currentFinancialYear) {
        List<BudgetItem> budgetItems = budgetItemRepository.findByFunctionAndCurrentFinancialYear(function, currentFinancialYear);
        return budgetItems;
    }

    public Boolean checkIfBudgetExistsForFunctionAndFinancialYear(CFunction function, CFinancialYear currentFinancialYear) {
        return budgetItemRepository.existsBudgetForCurrentFY(function.getId(), currentFinancialYear.getId());
    }


}
