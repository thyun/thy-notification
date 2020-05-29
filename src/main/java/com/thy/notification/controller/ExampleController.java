package com.thy.notification.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/examples")
public class ExampleController {
	private static final Logger logger = LoggerFactory.getLogger(ExampleController.class);

	@RequestMapping("/starter-template")
	public String starter_template(Model model) {
		model.addAttribute("message", "Hello there~~~~~~~~~~");
		return "starter-template/index";
	}

	@RequestMapping("/album")
	public String album(Model model) {
		model.addAttribute("message", "Hello there~~~~~~~~~~");
		return "album/index";
	}

	@RequestMapping("/dashboard")
	public String dashboard(Model model) {
		model.addAttribute("message", "Hello there~~~~~~~~~~");
		return "dashboard/index";
	}

	@RequestMapping("/sample01")
	public String sample01(Model model) {
		logger.debug("index() start");

		model.addAttribute("message", "Hello there~~~~~~~~~~");
		return "sample01";
	}

	@RequestMapping("/sample02")
	public String sample02(Model model, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("sample() start");
		return "sample02";
	}

}
