package com.nnk.springboot.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;

import jakarta.validation.Valid;

@Controller
public class RatingController {

	private final RatingService ratingService;

	public RatingController(RatingService ratingService) {
		this.ratingService = ratingService;
	}

	@GetMapping("/rating/list")
	public String home(Model model) {
		try {
			List<Rating> ratings = ratingService.getAllRatings();
			model.addAttribute("ratings", ratings);
			return "rating/list";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "rating/list";
		}

	}

	@GetMapping("/rating/add")
	public String addRatingForm(Rating rating) {
		return "rating/add";
	}

	@PostMapping("/rating/validate")
	public String validate(@Valid Rating rating, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "rating/add";
		}
		try {
			ratingService.saveRating(rating);
			return "redirect:/rating/list";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("rating", rating);
			return "rating/add";
		}
	}

	@GetMapping("/rating/update/{id}")
	public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
		try {
			Rating rating = ratingService.getRatingById(id);
			model.addAttribute("rating", rating);
			return "rating/update";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "rating/list";
		}
	}

	@PostMapping("/rating/update/{id}")
	public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return "rating/update";
		}

		try {
			ratingService.updateRating(id, rating);
			return "redirect:/rating/list";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "rating/update";
		}
	}

	@GetMapping("/rating/delete/{id}")
	public String deleteRating(@PathVariable("id") Integer id, Model model) {
		try {
			ratingService.deleteRatingById(id);
			return "redirect:/rating/list";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "rating/list";
		}
	}
}
