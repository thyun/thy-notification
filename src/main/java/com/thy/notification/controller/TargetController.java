package com.thy.notification.controller;

import com.thy.notification.entity.Target;
import com.thy.notification.entity.TargetRepository;
import com.thy.notification.entity.Webhook;
import com.thy.notification.service.TargetService;
import com.thy.notification.validation.TargetValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
@RequestMapping("/targets")
public class TargetController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TargetService targetService;
    private final TargetRepository targetRepository;
    private TargetValidator targetValidator;

    @Autowired
    public TargetController(TargetService targetService, TargetRepository targetRepository) {
        this.targetService = targetService;
        this.targetRepository = targetRepository;
        this.targetValidator = new TargetValidator(targetRepository);
    }

//    @InitBinder
//    protected void initBinder(WebDataBinder binder) {
//        binder.setValidator(new TargetValidator(targetRepository));
//    }

   @GetMapping("")
    public String index(Model model,
        @RequestParam(defaultValue = "") String search,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "key") String sort) {
        Page<Target> pagination = targetService.findByKeyStartsWith(search, page, size, sort);
        Pageable pageable = pagination.nextPageable();
        model.addAttribute("pagination", pagination);
        if (pagination.hasContent()) {
            model.addAttribute("items", pagination.getContent());
        } else
            model.addAttribute("items", new ArrayList<Target>());
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
        targetValidator.validate(target, result);
        if (result.hasErrors()) {
            logger.error(result.toString());
            return "targets/new-target";
        }
        logger.debug("create(): target=" + target.toString());

        target.trim();
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

        target.trim();
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
