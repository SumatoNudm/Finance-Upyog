package org.egov.egf.web.controller.budget;


import org.egov.commons.CFunction;
import org.egov.commons.service.FunctionService;
import org.egov.model.budget.BudgetHead;
import org.egov.model.budget.BudgetItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("/budget")
public class BudgetItemController {
    private static final String BUDGET_ITEM_NEW = "budgetitem-new";
    private static final String BUDGET_ITEM = "budgetItem";
    private static final String BUDGET_FORM = "budgetitem-form";

	@Autowired
	private FunctionService functionService;


    @RequestMapping(value = "/new", method = { RequestMethod.GET, RequestMethod.POST })
	public String newForm(final Model model) {
		model.addAttribute(BUDGET_ITEM, new BudgetItem());
		return BUDGET_ITEM_NEW;
	}


	@RequestMapping(value = "/form", method = {RequestMethod.POST})
	public String budgetForm(@ModelAttribute("functionId") Long functionId, final Model model) {
		CFunction function = functionService.findOne(functionId);
		model.addAttribute("function", function);
		return BUDGET_FORM;
	}

}
