package com.thy.notification.controller;

import com.thy.notification.entity.User;
import com.thy.notification.entity.UserRepository;
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
@RequestMapping("/users")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

   @GetMapping("")
    public String index(User user, Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "/users/index";
    }

    @GetMapping("/index-old")
    public String index_old(User user, Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users/index-old";
    }

    // TODO Implement users show
    @GetMapping("/show")
    public String show() {
        return "users/show-user";
    }

    @GetMapping("/new")
    public String _new(User user) {
        return "users/new-user";
    }

    @PostMapping("/create")
    public String create(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error(result.toString());
            return "users/new-user";
        }

        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "users/edit-user";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error(result.toString());
           return "users/edit-user";
        }

        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        return "redirect:/users";
    }
}
