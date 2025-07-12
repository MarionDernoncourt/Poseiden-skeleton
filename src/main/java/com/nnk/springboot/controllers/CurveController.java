package com.nnk.springboot.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.service.CurvePointService;

import jakarta.validation.Valid;

@Controller
public class CurveController {

	private final CurvePointService curvePointService;

	public CurveController(CurvePointService curvePointService) {
		this.curvePointService = curvePointService;
	}
	// TODO: Inject Curve Point service

	@RequestMapping("/curvePoint/list")
	public String home(Model model) {
		try {
			List<CurvePoint> curvePoints = curvePointService.getAllCurvePoints();
			model.addAttribute("curvePoints", curvePoints);
			return "curvePoint/list";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "curvePoint/list";

		}
	}

	@GetMapping("/curvePoint/add")
	public String addBidForm(CurvePoint bid) {
		return "curvePoint/add";
	}

	@PostMapping("/curvePoint/validate")
	public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "curvePoint/add";
		}
		try {
			curvePointService.saveCurvePoint(curvePoint);
			return "redirect:/curvePoint/list";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("curvePoint", curvePoint);
			return "curvePoint/add";
		}
	}

	@GetMapping("/curvePoint/update/{id}")
	public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
		try {
			CurvePoint curvePoint = curvePointService.getCurvePointById(id);
			model.addAttribute("curvePoint", curvePoint);
			return "curvePoint/update";

		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "curvePoint/list";
		}
	}

	@PostMapping("/curvePoint/update/{id}")
	public String updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint, BindingResult result,
			Model model) {
		if(result.hasErrors()) {
			return "curvePoint/update";
		}
		try {
			CurvePoint curvePointUpdated = curvePointService.updateCurvePoint(id, curvePoint);
			return "redirect:/curvePoint/list";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("curvePoint", curvePoint);
			return "curvePoint/update";
		}
		
	}

	@GetMapping("/curvePoint/delete/{id}")
	public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) {
		try {
			curvePointService.deleteCurvePointById(id);
			return "redirect:/curvePoint/list";
		} catch (Exception e ) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("curvePoints", curvePointService.getAllCurvePoints());
			return "curvePoint/list";
		}
		
	}
}
