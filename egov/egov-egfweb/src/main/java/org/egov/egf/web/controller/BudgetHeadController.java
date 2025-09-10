package org.egov.egf.web.controller;

import java.util.Arrays;
import java.util.Locale;

import javax.validation.Valid;

import org.egov.model.budget.BudgetGroup;
import org.egov.model.budget.BudgetHead;
import org.egov.model.service.BudgetHeadService;
import org.egov.model.service.BudgetingGroupService;
import org.egov.utils.BudgetAccountType;
import org.egov.utils.BudgetingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/budgethead")
public class BudgetHeadController {
	private static final String BUDGETHEAD_NEW = "budgethead-new";
    private static final String BUDGET_HEAD = "budgetHead";
    @Autowired
	private BudgetHeadService budgetHeadService;
    @Autowired
	private MessageSource messageSource;

    private void prepareNewForm(final Model model) {
		model.addAttribute("budgetAccountTypes", Arrays.asList(BudgetAccountType.values()));
	}

    @RequestMapping(value = "/new", method = { RequestMethod.GET, RequestMethod.POST })
	public String newForm(final Model model) {
		prepareNewForm(model);
        model.addAttribute(BUDGET_HEAD, new BudgetHead());
		return BUDGETHEAD_NEW;
	}

    @PostMapping(value = "/create")
	public String create(@Valid @ModelAttribute final BudgetHead budgetHead, final BindingResult errors,
			final RedirectAttributes redirectAttrs, final Model model) {

		budgetHeadService.create(budgetHead);
		redirectAttrs.addFlashAttribute("message",
				messageSource.getMessage("msg.budgetGroup.success", null, Locale.ENGLISH));
		return "redirect:/budgethead/new";
	}
    
}
