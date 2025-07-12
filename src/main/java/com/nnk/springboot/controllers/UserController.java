package com.nnk.springboot.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.UserService;
import com.nnk.springboot.validations.ValidationGroup.OnCreate;
import com.nnk.springboot.validations.ValidationGroup.OnUpdate;

import jakarta.validation.Valid;


@Controller
public class UserController {
	
    @Autowired
    private UserService userService;

    @GetMapping("/user/list")
    public String home(Model model) {
    	try {
    		List<User> users = userService.getAllUsers();
            model.addAttribute("users", users);
    	} catch (Exception e) {
    		model.addAttribute("error", e.getMessage());
    	}
    	return "user/list";
    }

    @GetMapping("/user/add")
    public String addUser(User bid) {
        return "user/add";
    }
    
    @PostMapping("user/validate")
    public String validate(@Validated(OnCreate.class) User user, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		return "user/add";
    	}
    	try {
    		userService.saveUser(user);
    		return "redirect:/user/list";
    	} catch (Exception e) {
    		model.addAttribute("error", e.getMessage());
    		model.addAttribute("user", user);
    		return "user/add";
    	}
    }

    
    @GetMapping("/user/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.name== @userService.getUserById(#id).getUsername()" )
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
    	try {
    		User user = userService.getUserById(id);
    		user.setPassword("");
    		model.addAttribute("user", user);
    		return "user/update";
    	}catch (Exception e) {
    		model.addAttribute("error", e.getMessage());
    		model.addAttribute("user", userService.getAllUsers());
    		return "user/list";
    	}
    }
    

    @PostMapping("/user/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.name== @userService.getUserById(#id).getUsername()" )
    public String updateUser(@PathVariable("id") Integer id, @Validated(OnUpdate.class) User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/update";
        }
        try {
        	userService.updateUserById(id, user);
        	return "redirect:/user/list";
        } catch (Exception e) {
        	model.addAttribute("error", e.getMessage());
        	model.addAttribute("user", user);
        	return "user/update";
        }
    }

    @GetMapping("/user/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
    	try {
    		userService.deleteUserById(id);
    		return "redirect:/user/list";
    	} catch (Exception e) {
    		model.addAttribute("error", e.getMessage());
    		return "user/list";
    	}
       
    }
}
