package com.nnk.springboot.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;

import jakarta.validation.Valid;

@Controller
public class RuleNameController {

	private final RuleNameService ruleNameService;

	public RuleNameController(RuleNameService ruleNameService) {
		this.ruleNameService = ruleNameService;
	}

	@GetMapping("/ruleName/list")
	public String home(Model model) {
		try {
			List<RuleName> ruleNames = ruleNameService.getAllRuleNames();
			model.addAttribute("ruleNames", ruleNames);
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}
		return "ruleName/list";
	}

	@GetMapping("/ruleName/add")
	public String addRuleForm(RuleName ruleName) {
		return "ruleName/add";
	}

	@PostMapping("/ruleName/validate")
	public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "ruleName/add";
		}
		try {
			ruleNameService.saveRuleName(ruleName);			
			return "redirect:/ruleName/list";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("ruleName", ruleName);
			return "ruleName/add";
		}

	}

	@GetMapping("/ruleName/update/{id}")
	public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
		try {
			RuleName ruleName = ruleNameService.getRuleNameById(id);
			model.addAttribute("ruleName", ruleName);
			return "ruleName/update";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "ruleName/list";
		}

	}

	@PostMapping("/ruleName/update/{id}")
	public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return "ruleName/update";
		}
		try {
			ruleNameService.updateRuleNameById(id, ruleName);
			return "redirect:/ruleName/list";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "ruleName/update";
		}
	}

	@GetMapping("/ruleName/delete/{id}")
	public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
		try {
			ruleNameService.deleteRuleNameById(id);
			return "redirect:/ruleName/list";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "ruleName/list";
		}

	}
}
