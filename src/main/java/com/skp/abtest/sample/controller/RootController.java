package com.skp.abtest.sample.controller;

import com.skp.abtest.sample.entity.Notification;
import com.skp.abtest.sample.entity.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class RootController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

   @GetMapping("")
    public String index() {
        return "redirect:/notifications";
    }
}
