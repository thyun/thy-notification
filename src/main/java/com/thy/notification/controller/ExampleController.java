package com.thy.notification.controller;

import com.thy.notification.entity.Checkout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/examples")
public class ExampleController {
	private static final Logger logger = LoggerFactory.getLogger(ExampleController.class);

	@RequestMapping("/starter-template")
	public String starter_template(Model model) {
		model.addAttribute("message", "Hello there~~~~~~~~~~");
		return "examples/starter-template/index";
	}

	@RequestMapping("/album")
	public String album(Model model) {
		model.addAttribute("message", "Hello there~~~~~~~~~~");
		return "examples/album/index";
	}

	@RequestMapping("/dashboard")
	public String dashboard(Model model) {
		model.addAttribute("message", "Hello there~~~~~~~~~~");
		return "examples/dashboard/index";
	}

	@RequestMapping("/checkout")
	public String checkout(Checkout checkout) {
		return "examples/checkout/index";
	}

	@RequestMapping("/checkout-simple")
	public String checkoutSimple(Checkout checkout) {
		return "examples/checkout/index-simple";
	}

	@RequestMapping("/checkout-submit")
	public String checkoutSubmit(@Valid Checkout checkout, BindingResult result, Model model) {
		model.addAttribute("message", "Hello there~~~~~~~~~~");
		result.rejectValue("firstName", "duplicate_value", "Duplicate value");
		return "examples/checkout/index";
	}

	@RequestMapping("/checkout-simple-submit")
	public String checkoutSimpleSubmit(@Valid Checkout checkout, BindingResult result, Model model) {
		model.addAttribute("message", "Hello there~~~~~~~~~~");
		result.rejectValue("firstName", "duplicate_value", "Duplicate value");
		return "examples/checkout/index-simple";
	}

	@RequestMapping("/sample01")
	public String sample01(Model model) {
		logger.debug("index() start");

		model.addAttribute("message", "Hello there~~~~~~~~~~");
		return "examples/sample/sample01";
	}

	@RequestMapping("/sample02")
	public String sample02(Model model, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("sample() start");
		return "examples/sample/sample02";
	}

	@RequestMapping("/login")
	public String login(Model model, HttpServletRequest request, HttpServletResponse response) {
		return "examples/sample/login";
	}

}
