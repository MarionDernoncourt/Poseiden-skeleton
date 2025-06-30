package com.nnk.springboot.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;

import jakarta.validation.Valid;

@Controller
public class TradeController {

	private final TradeService tradeService;

	public TradeController(TradeService tradeService) {
		this.tradeService = tradeService;
	}

	@RequestMapping("/trade/list")
	public String home(Model model) {
		try {
			List<Trade> trades = tradeService.getAllTrades();
			model.addAttribute("trades", trades);
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}
		return "trade/list";
	}

	@GetMapping("/trade/add")
	public String addUser(Trade bid) {
		return "trade/add";
	}

	@PostMapping("/trade/validate")
	public String validate(@Valid Trade trade, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "trade/add";
		}
		try {
			tradeService.saveTrade(trade);
			return "redirect:/trade/list";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "trade/add";
		}
	}

	@GetMapping("/trade/update/{id}")
	public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
		try {
			Trade trade = tradeService.getTradeById(id);
			model.addAttribute("trade", trade);
			return "trade/update";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "trade/list";
		}
	}

	@PostMapping("/trade/update/{id}")
	public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "trade/update";
		}
		try {
			Trade updatedTrade = tradeService.updateTradeById(id, trade);
			return "redirect:/trade/list";

		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "trade/update";
		}
	}

	@GetMapping("/trade/delete/{id}")
	public String deleteTrade(@PathVariable("id") Integer id, Model model) {
		try {
			tradeService.deleteTradeById(id);
			return "redirect:/trade/list";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "trade/list";
		}
	}
}
