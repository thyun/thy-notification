package com.thy.notification.validation;

import com.thy.notification.entity.TargetRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UrlOrNullValidator implements ConstraintValidator<UrlOrNull, String> {
    @Override
    public void initialize(UrlOrNull contactNumber) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext cxt) {
        if (value == null || value.length() == 0)
            return true;
        return isUrl(value);
        // return contactField != null && contactField.matches("[0-9]+") && (contactField.length() > 8) && (contactField.length() < 14);
    }

    public boolean isUrl(String url) {
        try {
            (new java.net.URL(url)).openStream().close();
            return true;
        } catch (Exception ex) { }
        return false;
    }

}

