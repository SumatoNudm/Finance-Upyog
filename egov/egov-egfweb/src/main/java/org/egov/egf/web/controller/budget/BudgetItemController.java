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
import org.egov.utils.BudgetAccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/budget")
public class BudgetItemController {
	private static final String BUDGET_ITEM_NEW = "budgetitem-new";
	private static final String BUDGET_ITEM = "budgetItem";
	private static final String BUDGET_FORM = "budgetitem-form";
	private static final String BUDGET_ITEM_VIEW = "budgetitem-view";


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
		CFinancialYear financialYear = financialYearService.getCurrentFinancialYear();
		ArrayList<String> errors = new ArrayList<>();
		// if (financialYear == null) {
		// // no current financial year found!
		// errors.add("Financial year not found !");
		// model.addAttribute("errors", errors);
		// return;
		// }

		if (financialYear == null) {
			model.addAttribute("errors", "Financial year not found !");
			return;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(financialYear.getEndingDate());
		calendar.add(Calendar.DATE, 1);
		CFinancialYear nextFinancialYear = financialYearService.getFinancialYearByDate(calendar.getTime());
		// if (nextFinancialYear == null) {
		// 	// return validation error stating no financial year
		// 	errors.add("Financial year not found for budget entry !");
		// 	model.addAttribute("errors", errors);
		// 	return;
		// }

		if (nextFinancialYear == null) {
			model.addAttribute("errors", "Financial year not found !");
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
		return "redirect:/budget/view/"+budgetForm.getFunctionid();
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

	@PostMapping(value = "/view/{functionId}")
	public String view(final Model model, @PathVariable Long functionId) throws Exception {

		final CFunction function = functionService.findOne(functionId);

		if (function == null) {
			throw new Exception("Selected function is invalid!");
		}

		final CFinancialYear currentFy = financialYearService.getCurrentFinancialYear();

		if (currentFy == null) {
			throw new Exception("Financial year is invalid !");
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentFy.getEndingDate());
		calendar.add(Calendar.DATE, 1);
		CFinancialYear nextFy = financialYearService.getFinancialYearByDate(calendar.getTime());

		if (nextFy == null) {
			throw new Exception("Invalid financial year ! for " + calendar.getTime());
		}


		model.addAttribute("currentFy", currentFy);
		model.addAttribute("nextFy", nextFy);







		List<String> types = Arrays.asList("Opening_Balance", "Closing_Balance", "Revenue_Budget", "Capital_Budget");
		Map<String,List<BudgetItem>> grouped = budgetItemService.getBudgetItemsByTypesFunctionFy(types, function, currentFy);


//		model.addAttribute("Opening_Balance", grouped.getOrDefault("Opening_Balance", Collections.emptyList()));
//		model.addAttribute("Closing_Balance", grouped.getOrDefault("Closing_Balance", Collections.emptyList()));
//		model.addAttribute("Revenue_Budget", grouped.getOrDefault("Revenue_Budget", Collections.emptyList()));
//		model.addAttribute("Capital_Budget", grouped.getOrDefault("Capital_Budget", Collections.emptyList()));
//
//		model.addAttribute("budgetGroups", grouped);


//		Map<String, Map<BudgetAccountType, Map<String, List<BudgetItem>>>> nestedGroup = new LinkedHashMap<>();
//
//		for (Map.Entry<String, List<BudgetItem>> entry : grouped.entrySet()) {
//
//			String type = entry.getKey();                 // "Opening_Balance"
//			List<BudgetItem> items = entry.getValue();    // list of BudgetItem for that type
//
//			if (shouldSkip(type, items)) {
//				continue;
//			}
//
//			// group by accountType then by category
//			Map<BudgetAccountType, Map<String, List<BudgetItem>>> byAccountAndCategory =
//					items.stream()
//							.collect(Collectors.groupingBy(
//									item -> item.getBudgetHead().getAccountType(),   // 1st group: accountType
//									Collectors.groupingBy(
//											item -> item.getBudgetHead().getCategory()   // 2nd group: category
//									)
//							));
//
//			nestedGroup.put(type, byAccountAndCategory);
//
//		}

//		model.addAttribute("nestedGroup", nestedGroup);


		final List<BudgetItem> oBal = grouped.getOrDefault("Opening_Balance", Collections.emptyList());
		final List<BudgetItem> cBal = grouped.getOrDefault("Closing_Balance", Collections.emptyList());
		final List<BudgetItem> rb = grouped.getOrDefault("Revenue_Budget", Collections.emptyList());
		final List<BudgetItem> cb = grouped.getOrDefault("Capital_Budget", Collections.emptyList());

		model.addAttribute("opening_balance", oBal);
		model.addAttribute("closing_balance", cBal);

		//grouping for revenue budget
		Map<BudgetAccountType, Map<String, List<BudgetItem>>> groupedRB = rb.stream().collect(Collectors.groupingBy(
				item -> item.getBudgetHead().getAccountType(),
				Collectors.groupingBy(
						itm -> itm.getBudgetHead().getCategory()
				)
		));

		model.addAttribute("grouped_rb", groupedRB);


		// grouping for capital budget
		Map<BudgetAccountType, Map<String, List<BudgetItem>>> groupedCB = cb.stream().collect(Collectors.groupingBy(
				item -> item.getBudgetHead().getAccountType(),
				Collectors.groupingBy(
						itm -> itm.getBudgetHead().getCategory()
				)
		));

		model.addAttribute("grouped_cb", groupedCB);


		Map<String, BudgetTotals> rbTotals = new LinkedHashMap<>();

		for (Map.Entry<BudgetAccountType, Map<String, List<BudgetItem>>> acct : groupedRB.entrySet()) {
			for (Map.Entry<String, List<BudgetItem>> cat : acct.getValue().entrySet()) {
				rbTotals.put(cat.getKey(), computeTotals(cat.getValue()));
			}
		}

		model.addAttribute("rbTotals", rbTotals);


		Map<String, BudgetTotals> cbTotals = new LinkedHashMap<>();

		for (Map.Entry<BudgetAccountType, Map<String, List<BudgetItem>>> acct : groupedCB.entrySet()) {
			for (Map.Entry<String, List<BudgetItem>> cat : acct.getValue().entrySet()) {
				cbTotals.put(cat.getKey(), computeTotals(cat.getValue()));
			}
		}

		model.addAttribute("cbTotals", cbTotals);


		return BUDGET_ITEM_VIEW;
	}

	private boolean shouldSkip(String type, List<BudgetItem> items) {
		return type == null
				|| items == null
				|| items.isEmpty()
				|| "Opening_Balance".equals(type)
				|| "Closing_Balance".equals(type);
	}


	private BudgetTotals computeTotals(List<BudgetItem> items) {

		BigDecimal est = items.stream()
				.map(BudgetItem::getCurrentEstimate)
				.filter(Objects::nonNull)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal act = items.stream()
				.map(BudgetItem::getCurrentActual)
				.filter(Objects::nonNull)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal rev = items.stream()
				.map(BudgetItem::getCurrentRevisedEstimate)
				.filter(Objects::nonNull)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal nxt = items.stream()
				.map(BudgetItem::getNextEstimate)
				.filter(Objects::nonNull)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return new BudgetTotals(est, act, rev, nxt);
	}




}
