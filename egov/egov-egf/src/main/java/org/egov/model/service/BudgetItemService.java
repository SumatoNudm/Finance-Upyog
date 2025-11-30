package org.egov.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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
    public BudgetItemService(final BudgetItemRepository budgetItemRepository,
            final FunctionRepository functionRepository) {
        this.budgetItemRepository = budgetItemRepository;
        this.functionRepository = functionRepository;
    }

    public BudgetItem create(BudgetItem item) {
        return budgetItemRepository.save(item);
    }

    @Transactional
    public BudgetItem update(BudgetItem item) {
        return budgetItemRepository.save(item);
    }

    @Transactional
    public void saveBudgetInputForm(BudgetForm form, Long budgetRegisterId) {

        try {
            // validate function
            CFunction function = functionRepository.findOne(form.getFunctionid());
            if (function == null) {
                throw new Exception("The selected function not found !");
            }

            BudgetRegister budgetRegister = budgetRegisterWorkflowService.findOne(budgetRegisterId);

            if (budgetRegister == null) {
                throw new Exception("Selected budget register is invalid or not available.");
            }


            // Validate financial years
            CFinancialYear financialYear = financialYearService.findOne(form.getFinancialYear());
            CFinancialYear nextFinancialYear = financialYearService.findOne(form.getCurrentFinancialYear());
            if (financialYear == null || nextFinancialYear == null) {
                throw new Exception("Financial year not found !");
            }

            // ---------------------------------
            // Save Opening Balance
            // ---------------------------------
            if (form.getOpening() != null) {
                BudgetItem opening = form.getOpening();
                opening.setBudgetGroup("Opening_Balance");
                opening.setFunction(function);
                opening.setFinancialYear(financialYear);
                opening.setCurrentFinancialYear(nextFinancialYear);

                opening.setBudgetRegister(budgetRegister);
                budgetItemRepository.save(opening);
            }

            // ---------------------------------
            // Running totals
            // ---------------------------------
            BigDecimal BudgetEstimateRevenue = BigDecimal.ZERO;
            BigDecimal ActualRevenue = BigDecimal.ZERO;
            BigDecimal RevisedEstimateRevenue = BigDecimal.ZERO;
            BigDecimal nextBudgetEstimateRevenue = BigDecimal.ZERO;

            BigDecimal BudgetEstimateExpenditure = BigDecimal.ZERO;
            BigDecimal ActualExpenditure = BigDecimal.ZERO;
            BigDecimal RevisedEstimateExpenditure = BigDecimal.ZERO;
            BigDecimal nextBudgetEstimateExpenditure = BigDecimal.ZERO;

            // ---------------------------------
            // Save Budget Items
            // ---------------------------------
            if (form.getItems() != null && !form.getItems().isEmpty()) {
                List<BudgetItem> items = form.getItems();

                for (BudgetItem item : items) {

                    if (item.getBudgetHead() == null || item.getBudgetHead().getId() == null) {
                        continue;
                    }

                    item.setFunction(function);
                    item.setFinancialYear(financialYear);
                    item.setCurrentFinancialYear(nextFinancialYear);

                    item.setBudgetRegister(budgetRegister);

                    // validating budget head
                    BudgetHead bh = budgetHeadService.findById(item.getBudgetHead().getId());
                    if (bh == null) {
                        throw new Exception("Invalid budget head on " + item.getBudgetGroup());
                    }
                    item.setBudgetHead(bh);

                    // validating scheme
                    if (item.getScheme() != null && item.getScheme().getId() != null) {
                        Scheme scheme = schemeHibernateDAO.getCurrentSession().load(Scheme.class,
                                item.getScheme().getId());
                        if (scheme == null) {
                            throw new Exception("Invalid scheme on " + item.getBudgetGroup());
                        }
                        item.setScheme(scheme);
                    } else {
                        item.setScheme(null);
                    }

                    // Categorize revenue / expenditure
                    final String code = item.getBudgetHead().getAccountTypeCode();
                    if (code == null)
                        continue;

                    switch (code) {
                        case "RR":
                        case "CR":
                            BudgetEstimateRevenue = BudgetEstimateRevenue.add(item.getCurrentEstimate());
                            ActualRevenue = ActualRevenue.add(item.getCurrentActual());
                            RevisedEstimateRevenue = RevisedEstimateRevenue.add(item.getCurrentRevisedEstimate());
                            nextBudgetEstimateRevenue = nextBudgetEstimateRevenue.add(item.getNextEstimate());
                            break;
                        case "RE":
                        case "CE":
                            BudgetEstimateExpenditure = BudgetEstimateExpenditure.add(item.getCurrentEstimate());
                            ActualExpenditure = ActualExpenditure.add(item.getCurrentActual());
                            RevisedEstimateExpenditure = RevisedEstimateExpenditure
                                    .add(item.getCurrentRevisedEstimate());
                            nextBudgetEstimateExpenditure = nextBudgetEstimateExpenditure.add(item.getNextEstimate());
                            break;
                        default:
                            break;
                    }

                    LOGGER.info("Budget Estimate Revenue:" + BudgetEstimateRevenue + ", Actual Revenue:" + ActualRevenue
                            + ", Revised Estimate Revenue:" + RevisedEstimateRevenue + ", Next Budget Estimate Revenue:"
                            + nextBudgetEstimateRevenue);
                    LOGGER.info("Budget Estimate Expenditure:" + BudgetEstimateExpenditure + ", Actual Expenditure:"
                            + ActualExpenditure + ", Revised Estimate Expenditure:" + RevisedEstimateExpenditure
                            + ", Next Budget Estimate Expenditure:" + nextBudgetEstimateExpenditure);

                    budgetItemRepository.save(item);
                }

            }

            // ---------------------------------
            // Compute Final Totals
            // ---------------------------------
            BigDecimal totalBudgetEstimate = BudgetEstimateRevenue.subtract(BudgetEstimateExpenditure);
            BigDecimal totalActual = ActualRevenue.subtract(ActualExpenditure);
            BigDecimal totalRevisedEstimate = RevisedEstimateRevenue.subtract(RevisedEstimateExpenditure);
            BigDecimal totalNextBudgetEstimate = nextBudgetEstimateRevenue.subtract(nextBudgetEstimateExpenditure);

            LOGGER.info("Budget Estimate:{}, Actual:{}, Revised Estimate:{}, Next Budget Estimate:{}",
                    totalBudgetEstimate, totalActual, totalRevisedEstimate, totalNextBudgetEstimate);

            BudgetItem openingBalance = form.getOpening();

            // ---------------------------------
            // Closing Balance
            // ---------------------------------
            BudgetItem closingBalance = new BudgetItem();
            closingBalance.setFunction(function);
            closingBalance.setFinancialYear(financialYear);
            closingBalance.setCurrentFinancialYear(nextFinancialYear);
            closingBalance.setBudgetGroup("Closing_Balance");
            closingBalance.setCurrentEstimate(openingBalance.getCurrentEstimate().add(totalBudgetEstimate));
            closingBalance.setCurrentActual(openingBalance.getCurrentActual().add(totalActual));
            closingBalance
                    .setCurrentRevisedEstimate(openingBalance.getCurrentRevisedEstimate().add(totalRevisedEstimate));
            closingBalance.setNextEstimate(openingBalance.getNextEstimate().add(totalNextBudgetEstimate));

            closingBalance.setBudgetRegister(budgetRegister);

            budgetItemRepository.save(closingBalance);

            // Save Closing Balance
            // if (form.getClosing() != null) {
            // BudgetItem closing = form.getClosing();
            // closing.setBudgetGroup("Closing_Balance");
            // closing.setFunction(function);
            // closing.setFinancialYear(financialYear);
            // closing.setCurrentFinancialYear(nextFinancialYear);
            // budgetItemRepository.save(closing);
            // }

            // final BudgetRegister budgetRegister = new BudgetRegister();
            // budgetRegister.setBudgetRegisterNumber("bud-2026-27-001");
            // budgetRegister.setFinancialYear(financialYearService.getCurrentFinancialYear());
            // budgetRegister.setBudgetType("RE");
            // budgetRegister.setStatus(egwStatusDAO.getStatusByModuleAndCode(FinancialConstants.BUDGET_MODULE,
            // FinancialConstants.BUDGET_CREATED_STATUS));

            // BudgetRegister saved = budgetRegisterWorkflowService.create(
            // budgetRegister,101L, "Initial submission for review", null, "START", "FMO"
            // );
            //
            //
            // LOGGER.info("Budget Register");
            // LOGGER.info("ID:{}", saved.getId());
            // LOGGER.info("Number:{}", saved.getBudgetRegisterNumber());
            // LOGGER.info("Workflow State:{}", saved.getCurrentState().getValue());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getFunctionWiseBudgetItems(final Long functionId, final Model model) {

    }

    // public List<BudgetItem> findByTypeAndFunctionIdAndFinancialYearId(String
    // type, CFunction function, Long fyId) {
    // return
    // budgetItemRepository.findByBudgetGroupAndCurrentFinancialYearIdAndFunction(type,
    // fyId, function);
    //
    // }

    public Map<String, List<BudgetItem>> getBudgetItemsByTypesFunctionFy(
            List<String> types, CFunction function, CFinancialYear financialYear) {

        List<BudgetItem> items = budgetItemRepository
                .findByBudgetGroupInAndFunctionAndCurrentFinancialYear(types, function, financialYear);

        // LOGGER.info("inside service!");
        // LOGGER.info(items.size());
        // items.forEach(i -> LOGGER.info(i.getBudgetCode()));

        return items.stream()
                .collect(Collectors.groupingBy(BudgetItem::getBudgetGroup));
    }

    public Map<String, List<BudgetItem>> getBudgetItemsByTypesFunctionFyBudgetRegister(
            List<String> types, CFunction function, CFinancialYear financialYear, BudgetRegister budgetRegister) {

        List<BudgetItem> items = budgetItemRepository
                .findByBudgetGroupInAndFunctionAndCurrentFinancialYearAndBudgetRegister(types, function, financialYear, budgetRegister);

        // LOGGER.info("inside service!");
        // LOGGER.info(items.size());
        // items.forEach(i -> LOGGER.info(i.getBudgetCode()));

        return items.stream()
                .collect(Collectors.groupingBy(BudgetItem::getBudgetGroup));
    }

    public List<BudgetItem> getBudgetItemsByFunctionAndCurrentFinancialYear(CFunction function,
            CFinancialYear currentFinancialYear) {
        List<BudgetItem> budgetItems = budgetItemRepository.findByFunctionAndCurrentFinancialYear(function,
                currentFinancialYear);
        return budgetItems;
    }

    public Boolean checkIfBudgetExistsForFunctionAndFinancialYear(CFunction function,
            CFinancialYear currentFinancialYear) {
        return budgetItemRepository.existsBudgetForCurrentFY(function.getId(), currentFinancialYear.getId());
    }

    public Boolean checkIfBudgetExistsForFunctionAndFinancialYearAndBudgetRegister(CFunction function,
                                                                                   CFinancialYear currentFinancialYear, BudgetRegister budgetRegister) {
        return budgetItemRepository.existsBudgetForCurrentFYAndBudgetRegister(function.getId(), currentFinancialYear.getId(), budgetRegister.getId());
    }

    private Boolean isExpenditure(String code) {
        return code.equalsIgnoreCase("re") || code.equalsIgnoreCase("ce");
    }

    private Boolean isRevenue(String code) {
        return code.equalsIgnoreCase("rr") || code.equalsIgnoreCase("cr");
    }

    public BigDecimal calculateClosingBalance(List<BudgetItem> items) {

        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal rr = BigDecimal.ZERO; // Revenue Receipts
        BigDecimal cr = BigDecimal.ZERO; // Capital Receipts
        BigDecimal re = BigDecimal.ZERO; // Revenue Expenditure
        BigDecimal ce = BigDecimal.ZERO; // Capital Expenditure

        for (BudgetItem item : items) {
            if (item == null || item.getBudgetHead() == null) {
                continue;
            }

            String code = item.getBudgetHead().getCode();
            if (code == null) {
                continue;
            }

            // choose which amount to use for calculation
            BigDecimal amount = item.getNextEstimate(); // or getCurrentRevisedEstimate(), etc.
            if (amount == null) {
                amount = BigDecimal.ZERO;
            }

            switch (code) {
                case "RR": // Revenue Receipts
                    rr = rr.add(amount);
                    break;
                case "CR": // Capital Receipts
                    cr = cr.add(amount);
                    break;
                case "RE": // Revenue Expenditure
                    re = re.add(amount);
                    break;
                case "CE": // Capital Expenditure
                    ce = ce.add(amount);
                    break;
                default:
                    // unknown code -> ignore or log
                    // LOGGER.warn("Unknown budget head code for closing balance: {}", code);
                    break;
            }
        }

        // closing balance = RR + CR - RE - CE
        return rr.add(cr).subtract(re).subtract(ce);
    }

    @Transactional
    public void updateBudgetInputForm(BudgetForm form) {

        try {

            // ========================================
            // 1️⃣ UPDATE OR INSERT OPENING BALANCE
            // ========================================

            BudgetItem opening = form.getOpening();
            if (opening != null) {

                BudgetItem openingBalance = budgetItemRepository.findOne(opening.getId());

                if (openingBalance == null) {
                    throw new Exception("opening balance is null");
                }

                LOGGER.info("saving:");
                LOGGER.info("my id:" + openingBalance.getId());

                openingBalance.setCurrentEstimate(opening.getCurrentEstimate());
                openingBalance.setCurrentActual(opening.getCurrentActual());
                openingBalance.setCurrentRevisedEstimate(opening.getCurrentRevisedEstimate());
                openingBalance.setNextEstimate(opening.getNextEstimate());

                budgetItemRepository.save(openingBalance);
            }

            // ---------------------------------
            // Running totals
            // ---------------------------------
            BigDecimal BudgetEstimateRevenue = BigDecimal.ZERO;
            BigDecimal ActualRevenue = BigDecimal.ZERO;
            BigDecimal RevisedEstimateRevenue = BigDecimal.ZERO;
            BigDecimal nextBudgetEstimateRevenue = BigDecimal.ZERO;

            BigDecimal BudgetEstimateExpenditure = BigDecimal.ZERO;
            BigDecimal ActualExpenditure = BigDecimal.ZERO;
            BigDecimal RevisedEstimateExpenditure = BigDecimal.ZERO;
            BigDecimal nextBudgetEstimateExpenditure = BigDecimal.ZERO;

            // ========================================
            // 2️⃣ MULTIPLE BUDGET ITEMS (ADD / UPDATE)
            // ========================================

            // Fetch function and financial years ONCE, not for every row
            CFunction function = functionRepository.findOne(form.getFunctionid());
            if (function == null) {
                throw new Exception("The selected function not found !");
            }

            CFinancialYear financialYear = financialYearService.findOne(form.getFinancialYear());
            CFinancialYear nextFinancialYear = financialYearService.findOne(form.getCurrentFinancialYear());

            if (financialYear == null || nextFinancialYear == null) {
                throw new Exception("Financial year not found !");
            }

            for (BudgetItem item : form.getItems()) {

                if (item == null)
                    continue;

                // NOTE: Some rows may be empty – skip them safely
                if (item.getBudgetHead() == null || item.getBudgetHead().getId() == null)
                    continue;

                // --- Validate Budget Head ---
                BudgetHead bh = budgetHeadService.findById(item.getBudgetHead().getId());
                if (bh == null) {
                    throw new Exception("Invalid budget head on " + item.getBudgetGroup());
                }
                item.setBudgetHead(bh);

                // --- Validate Scheme ---
                if (item.getScheme() != null && item.getScheme().getId() != null) {

                    Scheme scheme = schemeHibernateDAO.getCurrentSession()
                            .get(Scheme.class, item.getScheme().getId());

                    if (scheme == null) {
                        throw new Exception("Invalid scheme on " + item.getBudgetGroup());
                    }

                    item.setScheme(scheme); // valid scheme
                } else {
                    item.setScheme(null); // UI cleared → destroy scheme
                }

                // --------------------------------------------------------------------
                // FIX: New row detection (VERY IMPORTANT)
                // --------------------------------------------------------------------
                if (item.getId() == null || item.getId() == 0) {
                    // ---- INSERT NEW RECORD ----
                    LOGGER.info("Inserting new record → " + item.getBudgetCode());

                    item.setFunction(function);
                    item.setFinancialYear(financialYear);
                    item.setCurrentFinancialYear(nextFinancialYear);

                    budgetItemRepository.save(item);
                } else {

                    // ---- UPDATE EXISTING RECORD ----
                    BudgetItem budgetInput = budgetItemRepository.findOne(item.getId());

                    if (budgetInput == null) {
                        // fail-safe: if ID sent but record missing → treat as new
                        LOGGER.warn("ID sent but no record found. Creating as new.");
                        item.setId(null);
                        item.setFunction(function);
                        item.setFinancialYear(financialYear);
                        item.setCurrentFinancialYear(nextFinancialYear);
                        budgetItemRepository.save(item);
                        continue;
                    }

                    budgetInput.setCurrentEstimate(item.getCurrentEstimate());
                    budgetInput.setCurrentActual(item.getCurrentActual());
                    budgetInput.setCurrentRevisedEstimate(item.getCurrentRevisedEstimate());
                    budgetInput.setNextEstimate(item.getNextEstimate());

                    budgetInput.setBudgetCode(item.getBudgetCode());
                    budgetInput.setBudgetGroup(item.getBudgetGroup());
                    budgetInput.setBudgetHead(item.getBudgetHead());
                    budgetInput.setStateBudgetCode(item.getStateBudgetCode());

                    // Scheme logic
                    if (item.getScheme() == null) {
                        budgetInput.setScheme(null); // destroy old scheme
                    } else {
                        budgetInput.setScheme(item.getScheme()); // keep/update scheme
                    }

                    budgetItemRepository.save(budgetInput);
                }

                // --------------------------------------------------------------------
                // Categorization
                // --------------------------------------------------------------------
                final String code = item.getBudgetHead().getAccountTypeCode();
                if (code == null)
                    continue;

                switch (code) {

                    case "RR":
                    case "CR":
                        BudgetEstimateRevenue = BudgetEstimateRevenue.add(item.getCurrentEstimate());
                        ActualRevenue = ActualRevenue.add(item.getCurrentActual());
                        RevisedEstimateRevenue = RevisedEstimateRevenue.add(item.getCurrentRevisedEstimate());
                        nextBudgetEstimateRevenue = nextBudgetEstimateRevenue.add(item.getNextEstimate());
                        break;

                    case "RE":
                    case "CE":
                        BudgetEstimateExpenditure = BudgetEstimateExpenditure.add(item.getCurrentEstimate());
                        ActualExpenditure = ActualExpenditure.add(item.getCurrentActual());
                        RevisedEstimateExpenditure = RevisedEstimateExpenditure.add(item.getCurrentRevisedEstimate());
                        nextBudgetEstimateExpenditure = nextBudgetEstimateExpenditure.add(item.getNextEstimate());
                        break;
                }
            }

            // ---------------------------------
            // Compute Final Totals
            // ---------------------------------
            BigDecimal totalBudgetEstimate = BudgetEstimateRevenue.subtract(BudgetEstimateExpenditure);
            BigDecimal totalActual = ActualRevenue.subtract(ActualExpenditure);
            BigDecimal totalRevisedEstimate = RevisedEstimateRevenue.subtract(RevisedEstimateExpenditure);
            BigDecimal totalNextBudgetEstimate = nextBudgetEstimateRevenue.subtract(nextBudgetEstimateExpenditure);

            LOGGER.info("Budget Estimate:{}, Actual:{}, Revised Estimate:{}, Next Budget Estimate:{}",
                    totalBudgetEstimate, totalActual, totalRevisedEstimate, totalNextBudgetEstimate);

            BudgetItem openingBalance = budgetItemRepository.findOne(form.getOpening().getId());

            // ---------------------------------
            // Closing Balance
            // ---------------------------------
            BudgetItem closingBalance = budgetItemRepository.findByFunctionAndBudgetGroup(function, "Closing_Balance");

            closingBalance.setCurrentEstimate(openingBalance.getCurrentEstimate().add(totalBudgetEstimate));
            closingBalance.setCurrentActual(openingBalance.getCurrentActual().add(totalActual));
            closingBalance
                    .setCurrentRevisedEstimate(openingBalance.getCurrentRevisedEstimate().add(totalRevisedEstimate));
            closingBalance.setNextEstimate(openingBalance.getNextEstimate().add(totalNextBudgetEstimate));

            budgetItemRepository.save(closingBalance);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<CFunction> functionListWithBudget() {
        List<CFunction> functions = budgetItemRepository.findDistinctFunctionsWithBudgetItems();

        return functions;
    }

    public List<CFunction> functionsHavingBudgetOfBudgetRegister(BudgetRegister budgetRegister) {
        List<CFunction> functions = budgetItemRepository.findDistinctFunctionsByBudgetRegisterWithBudgetItems(budgetRegister.getId());

        return functions;
    }

}
