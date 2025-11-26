package org.egov.egf.web.controller.budget.register;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.service.CFinancialYearService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.model.budget.BudgetRegister;
import org.egov.model.service.BudgetRegisterWorkflowService;
import org.egov.utils.FinancialConstants;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/budget/register")
public class BudgetRegisterController extends GenericWorkFlowController {

    private static final String BUDGET_HEADER_NEW = "budgetheader-new";
    private static final String BUDGET_REGISTER_VIEW = "budgetregister-view";

    private static final Logger LOGGER = Logger.getLogger(BudgetRegisterController.class);

    private static final String STATE_TYPE = "stateType";

    private static final String APPROVAL_POSITION = "approvalPosition";

    private static final String APPROVAL_DESIGNATION = "approvalDesignation";


    @Autowired
    private CFinancialYearService financialYearService;

    @Autowired
    private BudgetRegisterWorkflowService budgetRegisterWorkflowService;

    @Autowired
    private CFinancialYearService cFinancialYearService;

    @Autowired
    private EgwStatusHibernateDAO egwStatusDAO;


    @RequestMapping(value = "/new", method = { RequestMethod.GET, RequestMethod.POST })
    public String newForm(final Model model,@ModelAttribute("budgetRegister") final BudgetRegister budgetRegister) {

        Map<String, CFinancialYear> financialYearMap = addFinancialYears(model);
        String name = "Budget_" + financialYearMap.get("nextFy").getFinYearRange();

        List<BudgetRegister> budgetRegisters =  budgetRegisterWorkflowService.findByFinancialYears(financialYearMap.get("currentFy"), financialYearMap.get("nextFy"));

        if (budgetRegisters != null || !budgetRegisters.isEmpty()) {
            model.addAttribute("error", "Budget is already created for the financial year !");
        }

//        BudgetRegister budgetRegister = new BudgetRegister();
        budgetRegister.setBudgetRegisterName(name);
        budgetRegister.setCurrentFinancialYear(financialYearMap.get("currentFy"));
        budgetRegister.setFinancialYear(financialYearMap.get("nextFy"));

        model.addAttribute("budgetRegister", budgetRegister);


        model.addAttribute(STATE_TYPE, budgetRegister.getClass().getSimpleName());

        prepareWorkflow(model, budgetRegister, new WorkflowContainer());

        return BUDGET_HEADER_NEW;
    }



    //public String create(final BudgetRegister budgetRegister, RedirectAttributes redirectAttributes, final HttpServletRequest request, @RequestParam @SafeHtml final String workFlowAction) {
    @PostMapping(value = "/create")
    public String create(final BudgetRegister budgetRegister, RedirectAttributes redirectAttributes, final HttpServletRequest request) {


        LOGGER.info("name:" + budgetRegister.getBudgetRegisterName() +", number: "+ budgetRegister.getBudgetRegisterNumber() + ", currentFy: " + budgetRegister.getCurrentFinancialYear().getId() + ", nextFy: " + budgetRegister.getFinancialYear().getId());

        final CFinancialYear currentFy = cFinancialYearService.findOne(budgetRegister.getCurrentFinancialYear().getId());
        final CFinancialYear nextFy = cFinancialYearService.findOne(budgetRegister.getFinancialYear().getId());

        budgetRegister.setCurrentFinancialYear(currentFy);
        budgetRegister.setFinancialYear(nextFy);

        budgetRegister.setBudgetRegisterNumber(budgetRegisterWorkflowService.generateBudgetRegisterNumber(nextFy.getFinYearRange()));

        budgetRegister.setStatus(egwStatusDAO.getStatusByModuleAndCode(FinancialConstants.BUDGET_MODULE, FinancialConstants.BUDGET_CREATED_NEW));



        budgetRegisterWorkflowService.initiateBudgetRegisterWf(budgetRegister);


//        Long approvalPosition = 0l;
//        String approvalComment = "";
//        String approvalDesignation = "";
//        if (request.getParameter("approvalComent") != null)
//            approvalComment = request.getParameter("approvalComent");
//        if (request.getParameter(APPROVAL_POSITION) != null && !request.getParameter(APPROVAL_POSITION).isEmpty())
//            approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POSITION));
//        if (request.getParameter(APPROVAL_DESIGNATION) != null
//                && !request.getParameter(APPROVAL_DESIGNATION).isEmpty())
//            approvalDesignation = String.valueOf(request.getParameter(APPROVAL_DESIGNATION));
//
//
//        LOGGER.info("comment:" + approvalComment + ", position: " + approvalPosition + ", designation: " + approvalDesignation);


        redirectAttributes.addAttribute("message", "Budget Register Created !");


        return "redirect:/budget/register/new";
    }



    @RequestMapping(value = "/view", method = {RequestMethod.GET, RequestMethod.POST})
    public String view(final Model model) {
        LOGGER.info("budget register view:");
        List<BudgetRegister> budgetRegisters = budgetRegisterWorkflowService.findBudgetRegisters();
        List<CFinancialYear> financialYears = financialYearService.getAllFinancialYears();

        LOGGER.info("Budget Register: ");
        budgetRegisters.forEach(budgetRegister -> {
            LOGGER.info("Number: " + budgetRegister.getBudgetRegisterNumber() + ", Name: " + budgetRegister.getBudgetRegisterName() + ", created Date: " + budgetRegister.getCreatedDate() + ", currentFy: " + budgetRegister.getCurrentFinancialYear().getFinYearRange() + ", next Fy: " + budgetRegister.getFinancialYear().getFinYearRange() + ", status: " + budgetRegister.getStatus().getCode()) ;
        });

        model.addAttribute("budgetRegisters", budgetRegisters);
        model.addAttribute("financialYears", financialYears);

        return BUDGET_REGISTER_VIEW;
    }



    private Map<String, CFinancialYear> addFinancialYears(Model model) {
        CFinancialYear financialYear = financialYearService.getCurrentFinancialYear();
        ArrayList<String> errors = new ArrayList<>();

        if (financialYear == null) {
            model.addAttribute("errors", "Financial year not found !");
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(financialYear.getEndingDate());
        calendar.add(Calendar.DATE, 1);
        CFinancialYear nextFinancialYear = financialYearService.getFinancialYearByDate(calendar.getTime());

        if (nextFinancialYear == null) {
            model.addAttribute("errors", "Financial year not found !");
            return null;
        }

        model.addAttribute("currentFy", financialYear);
        model.addAttribute("nextFy", nextFinancialYear);

        Map<String, CFinancialYear> financialYearMap = new HashMap<>();
        financialYearMap.put("currentFy", financialYear);
        financialYearMap.put("nextFy", nextFinancialYear);

        return financialYearMap;
    }








}
