package org.egov.egf.web.controller.budget;

import org.apache.log4j.Logger;
import org.egov.commons.CFunction;
import org.egov.commons.service.FunctionService;
import org.egov.egf.contract.model.Function;
import org.egov.egf.form.BudgetForm;
import org.egov.model.budget.BudgetHead;
import org.egov.model.budget.BudgetItem;
import org.egov.model.budget.Item;
import org.egov.model.budget.ItemForm;
import org.egov.model.service.BudgetItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/budget")
public class BudgetItemController {
	private static final String BUDGET_ITEM_NEW = "budgetitem-new";
	private static final String BUDGET_ITEM = "budgetItem";
	private static final String BUDGET_FORM = "budgetitem-form";

	private static final Logger LOGGER = Logger.getLogger(BudgetItemController.class);

	@Autowired
	private FunctionService functionService;

	@Autowired
	private BudgetItemService budgetItemService;


	@RequestMapping(value = "/new", method = { RequestMethod.GET, RequestMethod.POST })
	public String newForm(final Model model) {
		// model.addAttribute(BUDGET_ITEM, new BudgetItem());
		model.addAttribute("budgetForm", new BudgetForm());
		model.addAttribute("function", new CFunction());
		return BUDGET_ITEM_NEW;
	}

	// @RequestMapping(value = "/form", method = {RequestMethod.POST})
	// public String budgetForm(@ModelAttribute("functionId") Long functionId, final
	// Model model) {
	// CFunction function = functionService.findOne(functionId);
	// model.addAttribute("function", function);
	// return BUDGET_FORM;
	// }

	@RequestMapping(value = "/form", method = { RequestMethod.POST })
	public String budgetForm(@ModelAttribute("id") Long id, final Model model) {

		LOGGER.info("hello");

		CFunction function = functionService.findOne(id);
		LOGGER.info("Inside of budget form method");
		LOGGER.info("Function Id" + function.getId() + ", Name: " +
				function.getName() + ", code: " + function.getCode() + ", type: " +
				function.getType());
		model.addAttribute("function", function);

		ItemForm itemForm = new ItemForm();

		// Add one empty row by default
		itemForm.getItems().add(new Item());

		model.addAttribute("itemForm", itemForm);
		model.addAttribute("budgetForm", new BudgetForm());

		return BUDGET_FORM;
	}

	@PostMapping("/create")
	public String save(@ModelAttribute BudgetForm budgetForm, RedirectAttributes redirectAttrs) {

		LOGGER.info(budgetForm);
		budgetItemService.saveAll(budgetForm); // inside service: save opening, items, closing
		redirectAttrs.addFlashAttribute("message", "Budget items saved successfully!");
		return "redirect:/budget/new";
	}

}
