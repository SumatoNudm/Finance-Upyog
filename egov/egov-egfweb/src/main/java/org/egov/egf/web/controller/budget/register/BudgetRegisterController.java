package org.egov.egf.web.controller.budget.register;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CFinancialYearService;
import org.egov.model.budget.BudgetRegister;
import org.egov.model.service.BudgetRegisterWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/budget/register")
public class BudgetRegisterController {

    private static final String BUDGET_HEADER_NEW = "budgetheader-new";

    private static final Logger LOGGER = Logger.getLogger(BudgetRegisterController.class);


    @Autowired
    private CFinancialYearService financialYearService;

    @Autowired
    private BudgetRegisterWorkflowService budgetRegisterWorkflowService;


    @RequestMapping(value = "/new", method = { RequestMethod.GET, RequestMethod.POST })
    public String newForm(final Model model) {

        Map<String, CFinancialYear> financialYearMap = addFinancialYears(model);
        String name = "Budget_" + financialYearMap.get("nextFy").getFinYearRange();

        BudgetRegister budgetRegister = new BudgetRegister();
        budgetRegister.setBudgetRegisterName(name);
        budgetRegister.setCurrentFinancialYear(financialYearMap.get("currentFy"));
        budgetRegister.setFinancialYear(financialYearMap.get("nextFy"));

        model.addAttribute("budgetRegister", budgetRegister);
        return BUDGET_HEADER_NEW;
    }



    @PostMapping(value = "/create")
    public String create( final BudgetRegister budgetRegister, RedirectAttributes redirectAttributes) {


        LOGGER.info("name:" + budgetRegister.getBudgetRegisterName() +", number: "+ budgetRegister.getBudgetRegisterNumber() + ", currentFy: " + budgetRegister.getCurrentFinancialYear().getId() + ", nextFy: " + budgetRegister.getFinancialYear().getId());


//        budgetRegisterWorkflowService.create(budgetRegister)


        return "redirect:/budget/register/new";
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
