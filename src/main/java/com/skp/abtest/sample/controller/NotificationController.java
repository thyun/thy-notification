package com.skp.abtest.sample.controller;

import com.skp.abtest.sample.entity.Notification;
import com.skp.abtest.sample.entity.NotificationRepository;
import com.skp.abtest.sample.entity.User;
import com.skp.abtest.sample.entity.UserRepository;
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
@RequestMapping("/notifications")
public class NotificationController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

   @GetMapping("")
    public String index(Notification notification, Model model) {
        model.addAttribute("items", notificationRepository.findAll());
        return "notifications/index";
    }

    // TODO Implement notifications show
    @GetMapping("/show")
    public String show() {
        return "notifications/show-notification";
    }

    @GetMapping("/new")
    public String _new(Notification notification) {
        return "notifications/new-notification";
    }

    @PostMapping("/create")
    public String create(@Valid Notification notification, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error(result.toString());
            return "notifications/new-notification";
        }

        notificationRepository.save(notification);
        return "redirect:/notifications";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, Model model) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid notification Id:" + id));
        model.addAttribute("notification", notification);
        return "notifications/edit-notification";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") String id, @Valid Notification notification, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error(result.toString());
           return "notifications/edit-notification";
        }

        notificationRepository.save(notification);
        return "redirect:/notifications";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id, Model model) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid notification Id:" + id));
        notificationRepository.delete(notification);
        return "redirect:/notifications";
    }
}
