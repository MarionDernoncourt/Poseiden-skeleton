package com.nnk.springboot.controllers;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;

import jakarta.validation.Valid;

@Controller
public class BidListController {

	private final BidListService bidListService;

	public BidListController(BidListService bidListService) {
		this.bidListService = bidListService;
	}

	@GetMapping("/bidList/list")
	public String home(Model model) {
		try {
			List<BidList> bidLists = bidListService.getAllBidList();
			model.addAttribute("bidLists", bidLists);
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}
		return "bidList/list";
	}

	@GetMapping("/bidList/add")
	public String addBidForm(BidList bid) {
		return "bidList/add";
	}

	@PostMapping("/bidList/validate")
	public String validate(@Valid BidList bid, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "bidList/add";
		}
		try {
			bidListService.saveBidList(bid);
			return "redirect:/bidList/list";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("bidList", bid);
			return "bidList/add";
		}

	}

	@GetMapping("/bidList/update/{id}")
	public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
		try {
			BidList bidList = bidListService.getBidListById(id);
			model.addAttribute("bidList", bidList);
			return "bidList/update";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "bidList/list";
		}
	}

	@PostMapping("/bidList/update/{id}")
	public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "bidList/update";
		}
		try {
			bidListService.updateBidListById(id, bidList);
			return "redirect:/bidList/list";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "bidList/update";
		}
	}

	@GetMapping("/bidList/delete/{id}")
	public String deleteBid(@PathVariable("id") Integer id, Model model) {
		try {
			bidListService.deleteById(id);
			return "redirect:/bidList/list";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "bidList/list";
		}
	}
}
