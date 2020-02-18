package com.skp.abtest.sample.controller;

import com.skp.abtest.sample.entity.Target;
import com.skp.abtest.sample.entity.TargetRepository;
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
@RequestMapping("/targets")
public class TargetController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TargetRepository targetRepository;

    @Autowired
    public TargetController(TargetRepository targetRepository) {
        this.targetRepository = targetRepository;
    }

   @GetMapping("")
    public String index(Target target, Model model) {
        model.addAttribute("items", targetRepository.findAll());
        return "targets/index";
    }

    // TODO Implement notifications show
    @GetMapping("/show")
    public String show() {
        return "targets/show-target";
    }

    @GetMapping("/new")
    public String _new(Target target) {
        return "targets/new-target";
    }

    @PostMapping("/create")
    public String create(@Valid Target target, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error(result.toString());
            return "targets/new-target";
        }

        targetRepository.save(target);
        return "redirect:/targets";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, Model model) {
        Target target = targetRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid target id:" + id));
        model.addAttribute("target", target);
        return "targets/edit-target";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") String id, @Valid Target target, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error(result.toString());
           return "targets/edit-target";
        }

        targetRepository.save(target);
        return "redirect:/targets";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id, Model model) {
        Target target = targetRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid notification Id:" + id));
        targetRepository.delete(target);
        return "redirect:/targets";
    }
}
