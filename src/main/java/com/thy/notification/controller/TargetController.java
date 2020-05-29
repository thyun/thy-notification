package com.thy.notification.controller;

import com.thy.notification.entity.Target;
import com.thy.notification.entity.TargetRepository;
import com.thy.notification.entity.Webhook;
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
//        model.addAttribute("items", targetRepository.findAll(new Sort(Sort.Direction.ASC, "key")));
        return "targets/index";
    }

    // TODO Implement targets show
    @GetMapping("/show")
    public String show() {
        return "targets/show-target";
    }

    @GetMapping("/new")
    public String _new(Target target) {
        Webhook webhook = new Webhook();
        target.addWebhook(webhook);
        return "targets/new-target";
    }

    @PostMapping("/create")
    public String create(@Valid Target target, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error(result.toString());
            return "targets/new-target";
        }
        logger.debug("create(): target=" + target.toString());

        target.validate();
        targetRepository.save(target);
        return "redirect:/targets";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        Target target = targetRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid target id:" + id));
        logger.debug("edit(): target=" + target.toString());

        if (target.getWebhookList().size() == 0) {
            Webhook webhook = new Webhook();
            target.addWebhook(webhook);
        }
        model.addAttribute("target", target);
        return "targets/edit-target";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") String id, @Valid Target target, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error(result.toString());
           return "targets/edit-target";
        }

        target.validate();
        targetRepository.save(target);
        return "redirect:/targets";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, Model model) {
        Target target = targetRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid target Id:" + id));
        targetRepository.delete(target);
        return "redirect:/targets";
    }
}
