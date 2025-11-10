package org.egov.egf.web.controller.budget;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.service.CFinancialYearService;
import org.egov.commons.service.FunctionService;
import org.egov.egf.form.BudgetForm;
import org.egov.model.budget.*;
import org.egov.model.service.BudgetHeadService;
import org.egov.model.service.BudgetItemService;
import org.egov.model.service.FunctionBudgetHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

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

	@Autowired
	private CFinancialYearService financialYearService;

	@Autowired
	private BudgetHeadService budgetHeadService;

	@Autowired
	private FunctionBudgetHeadService functionBudgetHeadService;




	@RequestMapping(value = "/new", method = { RequestMethod.GET, RequestMethod.POST })
	public String newForm(final Model model) {
		// model.addAttribute(BUDGET_ITEM, new BudgetItem());
        prepareIfBudgetCanInput(model);
        model.addAttribute("function", new CFunction());
		return BUDGET_ITEM_NEW;
	}

	private void prepareIfBudgetCanInput(Model model) {
		addFinancialYears(model);
	}


	private void addFinancialYears(Model model) {
		CFinancialYear financialYear =  financialYearService.getCurrentFinancialYear();
		ArrayList<String> errors = new ArrayList<>();
		if (financialYear == null) {
			// no current financial year found!
			errors.add("Financial year not found !");
			model.addAttribute("errors", errors);
			return;
		}


		Calendar calendar = Calendar.getInstance();
		calendar.setTime(financialYear.getEndingDate());
		calendar.add(Calendar.DATE, 1);
		CFinancialYear nextFinancialYear = financialYearService.getFinancialYearByDate(calendar.getTime());
		if (nextFinancialYear == null) {
			// return validation error stating no financial year
			errors.add("Financial year not found for budget entry !");
			model.addAttribute("errors", errors);
			return;
		}

		model.addAttribute("currentFy", financialYear);
		model.addAttribute("nextFy", nextFinancialYear);
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
		model.addAttribute("budgetForm", new BudgetForm());

		ItemForm itemForm = new ItemForm();

		// Add one empty row by default
		itemForm.getItems().add(new Item());

		model.addAttribute("itemForm", itemForm);
		model.addAttribute("budgetForm", new BudgetForm());

		addFinancialYears(model);

		return BUDGET_FORM;
	}

	@PostMapping("/create")
	public String save(@ModelAttribute BudgetForm budgetForm, RedirectAttributes redirectAttrs) {

		LOGGER.info("opening bal entry \n\n");
		LOGGER.info(budgetForm.toString());
		budgetItemService.saveBudgetInputForm(budgetForm); // inside service: save opening, items, closing
		redirectAttrs.addFlashAttribute("message", "Budget items saved successfully!");
		return "redirect:/budget/new";
	}


	@RequestMapping(value = "/newv2", method = { RequestMethod.GET, RequestMethod.POST })
	public String newFormv2(final Model model) {
		// model.addAttribute(BUDGET_ITEM, new BudgetItem());
//		prepareIfBudgetCanInput(model);
		model.addAttribute("function", new CFunction());


		List<BudgetHead> heads = budgetHeadService.getActiveBudgetHeads();

		Map<String, List<BudgetHead>> grouped = heads.stream()
				.collect(Collectors.groupingBy(BudgetHead::getCategory, LinkedHashMap::new, Collectors.toList()));

		model.addAttribute("groupedHeads", grouped);

		return "functionwisebudget-form";
	}



}
