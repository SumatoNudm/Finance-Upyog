package org.egov.model.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Scheme;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.SchemeHibernateDAO;
import org.egov.commons.repository.FunctionRepository;
import org.egov.commons.service.CFinancialYearService;
import org.egov.egf.form.BudgetForm;
import org.egov.model.budget.BudgetHead;
import org.egov.model.budget.BudgetItem;
import org.egov.model.budget.BudgetRegister;
import org.egov.model.repository.BudgetItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class BudgetItemService {

    private final static Logger LOGGER = LoggerFactory.getLogger(BudgetItemService.class);

    private final BudgetItemRepository budgetItemRepository;

    private final FunctionRepository functionRepository;


    @Autowired
	private CFinancialYearService financialYearService;

    @Autowired
    private BudgetHeadService budgetHeadService;

    @Autowired
    private BudgetRegisterWorkflowService budgetRegisterWorkflowService;

    @Autowired
    private EgwStatusHibernateDAO egwStatusDAO;

    @Autowired
    private SchemeHibernateDAO schemeHibernateDAO;



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

                    try {
                        LOGGER.info("item: bh id:" + item.getBudgetHead().getId() + ", scheme id:" + item.getScheme().getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    item.setFunction(function);
                    item.setFinancialYear(financialYear);
                    item.setCurrentFinancialYear(nextFinancialYear);
                    BudgetHead bh= budgetHeadService.findById(item.getBudgetHead().getId());
                    if (bh == null) {
                        throw new Exception("Invalid budget head on " + item.getBudgetGroup());
                    }
                    item.setBudgetHead(bh);

                    if (item.getScheme() != null && item.getScheme().getId() != null) {
                        LOGGER.info("scheme is not null!");
//                        Scheme scheme = schemeHibernateDAO.getSchemeById(item.getScheme().getId());

                       Scheme scheme =  schemeHibernateDAO.getCurrentSession().load(Scheme.class, item.getScheme().getId());

                        if (scheme == null) {
                            throw new Exception("Invalid scheme on " + item.getBudgetGroup());
                        }

                        item.setScheme(scheme);
                    } else {
                        item.setScheme(null);
                    }

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


//            final BudgetRegister budgetRegister = new BudgetRegister();
//            budgetRegister.setBudgetRegisterNumber("bud-2026-27-001");
//            budgetRegister.setFinancialYear(financialYearService.getCurrentFinancialYear());
//            budgetRegister.setBudgetType("RE");
//            budgetRegister.setStatus(egwStatusDAO.getStatusByModuleAndCode(FinancialConstants.BUDGET_MODULE, FinancialConstants.BUDGET_CREATED_STATUS));


//           BudgetRegister saved =  budgetRegisterWorkflowService.create(
//                    budgetRegister,101L, "Initial submission for review", null, "START", "FMO"
//            );
//
//
//           LOGGER.info("Budget Register");
//            LOGGER.info("ID:{}", saved.getId());
//            LOGGER.info("Number:{}", saved.getBudgetRegisterNumber());
//            LOGGER.info("Workflow State:{}", saved.getCurrentState().getValue());



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
