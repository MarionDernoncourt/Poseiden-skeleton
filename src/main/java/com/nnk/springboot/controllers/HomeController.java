package com.nnk.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController
{
	@GetMapping("/")
	public String home(Model model)
	{
		return "home";
	}

	@GetMapping("/admin/home")
	public String adminHome(Model model)
	{
		return "redirect:/bidList/list";
	}

	@GetMapping("/access-denied")
	public String show403Page(Model model) {
		model.addAttribute("error", "Vous n'êtes pas autorisé à accèder à cette page");
		return "403";
	}

}
