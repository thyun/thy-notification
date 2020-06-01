package com.thy.notification.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique {

    String message() default "Duplicate value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    Class entity();
    String fieldName();
}

