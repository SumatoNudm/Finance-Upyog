package org.egov.egf.web.controller.budget;


import org.egov.model.budget.BudgetHead;
import org.egov.model.budget.BudgetItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/budget")
public class BudgetItemController {
    private static final String BUDGET_ITEM_NEW = "budgetitem-new";
    private static final String BUDGET_ITEM = "budgetItem";


    @RequestMapping(value = "/new", method = { RequestMethod.GET, RequestMethod.POST })
	public String newForm(final Model model) {
		model.addAttribute(BUDGET_ITEM, new BudgetItem());
		return BUDGET_ITEM_NEW;
	}
}
