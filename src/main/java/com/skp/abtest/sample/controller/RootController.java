package com.skp.abtest.sample.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class RootController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

   @GetMapping("")
    public String index() {
        return "redirect:/targets";
    }
}
