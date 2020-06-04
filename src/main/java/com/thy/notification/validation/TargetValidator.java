package com.thy.notification.validation;

import com.thy.notification.entity.Target;
import com.thy.notification.entity.TargetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

public class TargetValidator implements Validator {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private TargetRepository targetRepository;

    public TargetValidator(TargetRepository targetRepository) {
        this.targetRepository = targetRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Target.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        logger.debug("validate() start");
        Target target = (Target) obj;

        Optional<Target> o = targetRepository.findByKey(target.getKey());
        if (o.isPresent()) {
            errors.rejectValue("key", "duplicate_value", "Duplicate value");
        }
    }
}
