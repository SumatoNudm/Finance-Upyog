package org.egov.egf.web.controller.budget;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.service.CFinancialYearService;
import org.egov.commons.service.FunctionService;
import org.egov.egf.form.BudgetForm;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.microservice.models.EmployeeInfo;
import org.egov.infra.microservice.utils.MicroserviceUtils;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.model.budget.*;
import org.egov.model.service.BudgetHeadService;
import org.egov.model.service.BudgetItemService;
import org.egov.model.service.BudgetRegisterWorkflowService;
import org.egov.model.service.FunctionBudgetHeadService;
import org.egov.utils.BudgetAccountType;
import org.egov.utils.FinancialConstants;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
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
	private static final String BUDGET_ITEM_EDIT = "budgetitem-edit";
	private static final String BUDGET_FUNCTION = "budgetitem-function";
	private static final String BUDGET_COMPLETE_VIEW = "budgetitemcomplete-view";

	private static final String STATE_TYPE = "stateType";

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

	@Autowired
	private BudgetRegisterWorkflowService budgetRegisterWorkflowService;

	@Autowired
	private MicroserviceUtils microServiceUtil;

	@Autowired
	private SecurityUtils securityUtils;


	@RequestMapping(value = "/new/{budgetRegisterId}", method = { RequestMethod.GET, RequestMethod.POST })
	public String newForm(final Model model, @PathVariable("budgetRegisterId") Long budgetRegisterId) {
		// model.addAttribute(BUDGET_ITEM, new BudgetItem());
		prepareIfBudgetCanInput(model);
		model.addAttribute("function", new CFunction());

		BudgetRegister budgetRegister = budgetRegisterWorkflowService.findOne(budgetRegisterId);

		if (budgetRegister == null) {
			model.addAttribute("error", "Selected Budget register not available or invalid.");
		}

		model.addAttribute("budgetRegisterId", budgetRegisterId);
		return BUDGET_ITEM_NEW;
	}

	private void prepareIfBudgetCanInput(Model model) {
		addFinancialYears(model);
	}

	private Map<String, CFinancialYear> addFinancialYears(Model model) {
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
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(financialYear.getEndingDate());
		calendar.add(Calendar.DATE, 1);
		CFinancialYear nextFinancialYear = financialYearService.getFinancialYearByDate(calendar.getTime());
		// if (nextFinancialYear == null) {
		// // return validation error stating no financial year
		// errors.add("Financial year not found for budget entry !");
		// model.addAttribute("errors", errors);
		// return;
		// }

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

	// @RequestMapping(value = "/form", method = {RequestMethod.POST})
	// public String budgetForm(@ModelAttribute("functionId") Long functionId, final
	// Model model) {
	// CFunction function = functionService.findOne(functionId);
	// model.addAttribute("function", function);
	// return BUDGET_FORM;
	// }

	@RequestMapping(value = "/form", method = { RequestMethod.POST })
	public String budgetForm(@ModelAttribute("id") Long id, @ModelAttribute("budgetRegisterId") Long budgetRegisterId, final Model model, RedirectAttributes redirectAttributes) {

		LOGGER.info("hello");
		LOGGER.info("budget register id:" + budgetRegisterId);

		Map<String, CFinancialYear> financialYears = addFinancialYears(model);

		if (financialYears == null || financialYears.size() < 2) {
			return "budget/new/"+budgetRegisterId;
		}

		CFunction function = functionService.findOne(id);

		BudgetRegister budgetRegister = budgetRegisterWorkflowService.findOne(budgetRegisterId);

		if (budgetRegister == null) {
			redirectAttributes.addAttribute("error", "Selected Budget register not available or invalid.");
			return "redirect:/budget/new/"+budgetRegisterId;
		}

		model.addAttribute("budgetRegisterId", budgetRegisterId);


		Boolean budgetAlreadyEntered = checkIfBudgetAlreadyEntered(function, financialYears, budgetRegister);

		if (Boolean.TRUE.equals(budgetAlreadyEntered)) {
			redirectAttributes.addFlashAttribute("error", "Budget already entered for the selected function.");
			return "redirect:/budget/new/"+budgetRegisterId;
		}


		model.addAttribute("function", function);

		// List<BudgetItem> budgetItems =
		// functionBudgetHeadService.functionBudgetHeads(function.getId()).stream().map(fbh
		// -> {
		// BudgetItem budgetItem = new BudgetItem();
		// budgetItem.setBudgetHead(fbh.getBudgetHead());
		// budgetItem.setFunction(fbh.getFunction());
		// budgetItem.setFinancialYear(financialYears.get("nextFy"));
		// budgetItem.setCurrentFinancialYear(financialYears.get("currentFy"));
		// return budgetItem;
		// }).collect(Collectors.toList());

		// BudgetForm budgetForm = new BudgetForm();
		// budgetForm.setItems(budgetItems);

		List<BudgetHead> budgetHeads = functionBudgetHeadService.functionBudgetHeads(function.getId()).stream()
				.map(FunctionBudgetHead::getBudgetHead).collect(Collectors.toList());

		model.addAttribute("budgetHeads", budgetHeads);

		model.addAttribute("budgetForm", new BudgetForm());

		// BudgetRegister budgetRegister = new BudgetRegister();

		// model.addAttribute(STATE_TYPE, "BudgetRegister");
		// prepareWorkflow(model, budgetRegister, new WorkflowContainer());

		addFinancialYears(model);

		return BUDGET_FORM;
	}

	private Boolean checkIfBudgetAlreadyEntered(CFunction function, Map<String, CFinancialYear> financialYears, BudgetRegister budgetRegister) {
		final CFinancialYear currentFy = financialYears.get("currentFy");
//		Boolean budgetExists = budgetItemService.checkIfBudgetExistsForFunctionAndFinancialYear(function, currentFy);
		Boolean budgetExists = budgetItemService.checkIfBudgetExistsForFunctionAndFinancialYearAndBudgetRegister(function, currentFy, budgetRegister);
		return budgetExists;
	}

	@PostMapping("/create")
	public String save(@ModelAttribute BudgetForm budgetForm, @ModelAttribute("budgetRegisterId") Long budgetRegisterId, RedirectAttributes redirectAttrs,
			final HttpServletRequest request) {

		LOGGER.info("opening bal entry \n\n");
		LOGGER.info(budgetForm.getFunctionid());
		budgetItemService.saveBudgetInputForm(budgetForm, budgetRegisterId); // inside service: save opening, items, closing
		redirectAttrs.addFlashAttribute("message", "Budget items saved successfully!");

		return "forward:/budget/view/" + budgetForm.getFunctionid()+"/"+budgetRegisterId;
	}

	@RequestMapping(value = "/newv2", method = { RequestMethod.GET, RequestMethod.POST })
	public String newFormv2(final Model model) {
		// model.addAttribute(BUDGET_ITEM, new BudgetItem());
		// prepareIfBudgetCanInput(model);
		model.addAttribute("function", new CFunction());

		List<BudgetHead> heads = budgetHeadService.getActiveBudgetHeads();

		Map<String, List<BudgetHead>> grouped = heads.stream()
				.collect(Collectors.groupingBy(BudgetHead::getCategory, LinkedHashMap::new, Collectors.toList()));

		model.addAttribute("groupedHeads", grouped);

		return "functionwisebudget-form";
	}

	@RequestMapping(value = "/view/{functionId}/{budgetRegisterId}", method = {RequestMethod.GET, RequestMethod.POST})
	public String view(final Model model, @PathVariable Long functionId,  @PathVariable("budgetRegisterId") Long budgetRegisterId, RedirectAttributes redirectAttributes) throws Exception {

		final CFunction function = functionService.findOne(functionId);

		if (function == null) {
			throw new Exception("Selected function is invalid!");
		}

		model.addAttribute("function", function);

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


		BudgetRegister budgetRegister = budgetRegisterWorkflowService.findOne(budgetRegisterId);

		if (budgetRegister == null) {
			redirectAttributes.addAttribute("error", "Selected Budget register not available or invalid.");
			return "redirect:/budget/new";
		}

		model.addAttribute("budgetRegisterId", budgetRegisterId);


		List<String> types = Arrays.asList("Opening_Balance", "Closing_Balance", "Revenue_Budget", "Capital_Budget");
		Map<String, List<BudgetItem>> grouped = budgetItemService.getBudgetItemsByTypesFunctionFyBudgetRegister(types, function,
				currentFy, budgetRegister);

		// model.addAttribute("Opening_Balance", grouped.getOrDefault("Opening_Balance",
		// Collections.emptyList()));
		// model.addAttribute("Closing_Balance", grouped.getOrDefault("Closing_Balance",
		// Collections.emptyList()));
		// model.addAttribute("Revenue_Budget", grouped.getOrDefault("Revenue_Budget",
		// Collections.emptyList()));
		// model.addAttribute("Capital_Budget", grouped.getOrDefault("Capital_Budget",
		// Collections.emptyList()));
		//
		// model.addAttribute("budgetGroups", grouped);

		// Map<String, Map<BudgetAccountType, Map<String, List<BudgetItem>>>>
		// nestedGroup = new LinkedHashMap<>();
		//
		// for (Map.Entry<String, List<BudgetItem>> entry : grouped.entrySet()) {
		//
		// String type = entry.getKey(); // "Opening_Balance"
		// List<BudgetItem> items = entry.getValue(); // list of BudgetItem for that
		// type
		//
		// if (shouldSkip(type, items)) {
		// continue;
		// }
		//
		// // group by accountType then by category
		// Map<BudgetAccountType, Map<String, List<BudgetItem>>> byAccountAndCategory =
		// items.stream()
		// .collect(Collectors.groupingBy(
		// item -> item.getBudgetHead().getAccountType(), // 1st group: accountType
		// Collectors.groupingBy(
		// item -> item.getBudgetHead().getCategory() // 2nd group: category
		// )
		// ));
		//
		// nestedGroup.put(type, byAccountAndCategory);
		//
		// }

		// model.addAttribute("nestedGroup", nestedGroup);

		final List<BudgetItem> oBal = grouped.getOrDefault("Opening_Balance", Collections.emptyList());
		final List<BudgetItem> cBal = grouped.getOrDefault("Closing_Balance", Collections.emptyList());
		final List<BudgetItem> rb = grouped.getOrDefault("Revenue_Budget", Collections.emptyList());
		final List<BudgetItem> cb = grouped.getOrDefault("Capital_Budget", Collections.emptyList());

		model.addAttribute("opening_balance", oBal);
		model.addAttribute("closing_balance", cBal);

		// grouping for revenue budget
		Map<BudgetAccountType, Map<String, List<BudgetItem>>> groupedRB = rb.stream().collect(Collectors.groupingBy(
				item -> item.getBudgetHead().getAccountType(),
				Collectors.groupingBy(
						itm -> itm.getBudgetHead().getCategory())));

		model.addAttribute("grouped_rb", groupedRB);

		// grouping for capital budget
		Map<BudgetAccountType, Map<String, List<BudgetItem>>> groupedCB = cb.stream().collect(Collectors.groupingBy(
				item -> item.getBudgetHead().getAccountType(),
				Collectors.groupingBy(
						itm -> itm.getBudgetHead().getCategory())));

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

	@RequestMapping(value = "/edit/{functionId}/{budgetRegisterId}", method = {RequestMethod.GET, RequestMethod.POST})
	public String edit(@PathVariable Long functionId,  @PathVariable("budgetRegisterId") Long budgetRegisterId, Model model, RedirectAttributes redirectAttributes) throws Exception {

		final CFunction function = functionService.findOne(functionId);

		if (function == null) {
			throw new Exception("Selected function is invalid!");
		}

		model.addAttribute("function", function);

		BudgetRegister budgetRegister = budgetRegisterWorkflowService.findOne(budgetRegisterId);

		if (budgetRegister == null) {
			redirectAttributes.addAttribute("error", "Selected Budget register not available or invalid.");
			return "redirect:/budget/new";
		}

		model.addAttribute("budgetRegisterId", budgetRegisterId);
		model.addAttribute("budgetRegister", budgetRegister);


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

		//model.addAttribute("budgetForm", new BudgetForm());

		List<String> types = Arrays.asList("Opening_Balance", "Closing_Balance", "Revenue_Budget", "Capital_Budget");
		Map<String, List<BudgetItem>> grouped = budgetItemService.getBudgetItemsByTypesFunctionFyBudgetRegister(types, function,
				currentFy, budgetRegister);

		// final List<BudgetItem> oBal = grouped.getOrDefault("Opening_Balance",
		// Collections.emptyList());
		// final List<BudgetItem> cBal = grouped.getOrDefault("Closing_Balance",
		// Collections.emptyList());

		// model.addAttribute("opening_balance", oBal.get(0));
		// model.addAttribute("closing_balance", cBal);

		List<BudgetItem> oBal = grouped.getOrDefault("Opening_Balance", Collections.emptyList());
		//BudgetItem first = oBal.isEmpty() ? new BudgetItem() : oBal.get(0);

		//model.addAttribute("opening_balance", oBal);

		List<BudgetItem> revenue = grouped.getOrDefault("Revenue_Budget", Collections.emptyList());
		List<BudgetItem> capital = grouped.getOrDefault("Capital_Budget", Collections.emptyList());

		// merge both lists into one
		List<BudgetItem> allBudget = new ArrayList<>();
		allBudget.addAll(revenue);
		allBudget.addAll(capital);

		// model.addAttribute("opening_balance", oBal);
		// model.addAttribute("closing_balance", cBal);
		model.addAttribute("all_budget_items", allBudget);

		BudgetForm form = new BudgetForm();
		form.setOpening(oBal.get(0)); // <-- FIX
		form.setItems(allBudget); // <-- items also must be set
		form.setFunctionid(function.getId());
		form.setCurrentFinancialYear(currentFy.getId());
		form.setFinancialYear(nextFy.getId());

		model.addAttribute("budgetForm", form);

		model.addAttribute("function", function);
		model.addAttribute("currentFy", currentFy);
		model.addAttribute("nextFy", nextFy);

		System.out.println("Opening in GET = " + form.getOpening().getId());

		return BUDGET_ITEM_EDIT;
	}

	@PostMapping("/update/{budgetRegisterId}")
	public String update(@ModelAttribute BudgetForm budgetForm,  @PathVariable("budgetRegisterId") Long budgetRegisterId, RedirectAttributes redirectAttrs) {

		try {

			BudgetRegister budgetRegister = budgetRegisterWorkflowService.findOne(budgetRegisterId);

			if (budgetRegister == null) {
				redirectAttrs.addAttribute("error", "Selected Budget register not available or invalid.");
				return "redirect:/budget/new";
			}

			LOGGER.info("update form \n\n");
			LOGGER.info(budgetForm.getOpening().getId());
			LOGGER.info(budgetForm.getFunctionid());
			LOGGER.info(budgetForm.getCurrentFinancialYear());
			LOGGER.info(budgetForm.getFinancialYear());

			System.out.println("Opening in POST = " + budgetForm.getOpening().getId());

			budgetItemService.updateBudgetInputForm(budgetForm, budgetRegister); // inside service: save opening, items, closing
			redirectAttrs.addFlashAttribute("message", "Budget items updated successfully!");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/budget/view/" + budgetForm.getFunctionid() + "/"+ budgetRegisterId;
	}


	@RequestMapping(value = "/functionwise/{budgetRegisterId}", method = {RequestMethod.GET, RequestMethod.POST})
	public String functionView(final Model model, @PathVariable("budgetRegisterId") Long budgetRegisterId, RedirectAttributes redirectAttributes){

		BudgetRegister budgetRegister = budgetRegisterWorkflowService.findOne(budgetRegisterId);

		if (budgetRegister == null) {
			model.addAttribute("error", "Selected Budget register not available or invalid.");
			redirectAttributes.addAttribute("error", "Selected Budget register not available or invalid.");
			return "";
		}

		model.addAttribute("budgetRegisterId", budgetRegisterId);
		model.addAttribute("budgetRegister", budgetRegister);

//		List<CFunction> budgetFunction = budgetItemService.functionListWithBudget();

		List<CFunction> budgetFunction = budgetItemService.functionsHavingBudgetOfBudgetRegister(budgetRegister);

		model.addAttribute("budgetFunction", budgetFunction);

		User currentUser = securityUtils.getCurrentUser();

		List<EmployeeInfo> emplist = microServiceUtil.getEmployee(currentUser.getId(), null, null, null);

		LOGGER.info("emplist: " + emplist.size());

		String[] allowedStatus =  new String[]{"reverted", "REVERTED", "NEW", "new"};

		if (Arrays.asList(allowedStatus).contains(budgetRegister.getStatus().getCode().toLowerCase())) {
			if (emplist != null && !emplist.isEmpty()) {
				String designation = emplist.get(0).getAssignments().get(0).getDesignation();
				LOGGER.info("emp-des: " + designation);
				String[] desigs = new String[]{"Financial Management Officer", "FMO", "Accounts Officer", "AO"};
				if (Arrays.asList(desigs).contains(designation)) {
					model.addAttribute("allowCreate", true);
				}
			}
		}



		return BUDGET_FUNCTION;

	}

	@RequestMapping(value = "/complete/{budgetRegisterId}/view", method = {RequestMethod.GET, RequestMethod.POST})
	public String completeBudgetView(final Model model, @PathVariable("budgetRegisterId") Long budgetRegisterId, RedirectAttributes redirectAttributes) {

		BudgetRegister budgetRegister = budgetRegisterWorkflowService.findOne(budgetRegisterId);

		if (budgetRegister == null) {
			redirectAttributes.addAttribute("error", "Selected Budget register not available or invalid.");
			return "redirect:/budget/new";
		}

		model.addAttribute("budgetRegisterId", budgetRegisterId);
		model.addAttribute("budgetRegister", budgetRegister);

		addFinancialYears(model);


		List<String> types = Arrays.asList("Opening_Balance", "Closing_Balance", "Revenue_Budget", "Capital_Budget");
		Map<String, List<BudgetItem>> grouped = budgetItemService.getBudgetItemsByTypesAndBudgetRegister(types,
				 budgetRegister);

		final List<BudgetItem> oBal = grouped.getOrDefault("Opening_Balance", Collections.emptyList());
		final List<BudgetItem> cBal = grouped.getOrDefault("Closing_Balance", Collections.emptyList());
		final List<BudgetItem> rb = grouped.getOrDefault("Revenue_Budget", Collections.emptyList());
		final List<BudgetItem> cb = grouped.getOrDefault("Capital_Budget", Collections.emptyList());


		BigDecimal openingCurrentEstimate = BigDecimal.ZERO;
		BigDecimal openingActual = BigDecimal.ZERO;
		BigDecimal openingRevised = BigDecimal.ZERO;
		BigDecimal openingNext = BigDecimal.ZERO;

		for (BudgetItem ob : oBal) {
			openingCurrentEstimate = openingCurrentEstimate.add(ob.getCurrentEstimate());
			openingActual = openingActual.add(ob.getCurrentActual());
			openingRevised = openingRevised.add(ob.getCurrentRevisedEstimate());
			openingNext = openingNext.add(ob.getNextEstimate());
		}

		BudgetItem openingBalance = new BudgetItem();
		openingBalance.setCurrentEstimate(openingCurrentEstimate);
		openingBalance.setCurrentActual(openingActual);
		openingBalance.setCurrentRevisedEstimate(openingRevised);
		openingBalance.setNextEstimate(openingNext);

		LOGGER.info("ce: " + openingBalance.getCurrentEstimate() +", ca: "+ openingBalance.getCurrentActual() + ", cr: "+ openingBalance.getCurrentRevisedEstimate() + ", ne: "+ openingBalance.getNextEstimate());

		//closing
		BigDecimal closingCurrentEstimate = BigDecimal.ZERO;
		BigDecimal closingActual = BigDecimal.ZERO;
		BigDecimal closingRevised = BigDecimal.ZERO;
		BigDecimal closingNext = BigDecimal.ZERO;

		for (BudgetItem cbalance : cBal) {
			closingCurrentEstimate = closingCurrentEstimate.add(cbalance.getCurrentEstimate());
			closingActual = closingActual.add(cbalance.getCurrentActual());
			closingRevised = closingRevised.add(cbalance.getCurrentRevisedEstimate());
			closingNext = closingNext.add(cbalance.getNextEstimate());
		}

		BudgetItem closingBalance = new BudgetItem();
		closingBalance.setCurrentEstimate(closingCurrentEstimate);
		closingBalance.setCurrentActual(closingActual);
		closingBalance.setCurrentRevisedEstimate(closingRevised);
		closingBalance.setNextEstimate(closingNext);

		model.addAttribute("opening_balance", openingBalance);
		model.addAttribute("closing_balance", closingBalance);



		// grouping for revenue budget
		Map<BudgetAccountType, Map<String, List<BudgetItem>>> groupedRB = rb.stream().collect(Collectors.groupingBy(
				item -> item.getBudgetHead().getAccountType(),
				Collectors.groupingBy(
						itm -> itm.getBudgetHead().getCategory())));

		model.addAttribute("grouped_rb", groupedRB);

		// grouping for capital budget
		Map<BudgetAccountType, Map<String, List<BudgetItem>>> groupedCB = cb.stream().collect(Collectors.groupingBy(
				item -> item.getBudgetHead().getAccountType(),
				Collectors.groupingBy(
						itm -> itm.getBudgetHead().getCategory())));

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

		return BUDGET_COMPLETE_VIEW;
	}


}



