package com.thy.notification.validation;

import com.thy.notification.entity.Target;
import com.thy.notification.entity.TargetRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

/*
 * Do not used this validator - @Autowired does not work
 */
public class UniqueValidator implements ConstraintValidator<Unique, Object> {
   @Autowired
   private TargetRepository targetRepository;

   private Class clazz;
   private String fieldName;

   public void initialize(Unique constraint) {
      this.clazz = constraint.entity();
      this.fieldName = constraint.fieldName();
   }

   public boolean isValid(Object obj, ConstraintValidatorContext context) {
      if (clazz.getName().equals(Target.class.getName()) && fieldName.equals("key")) {
 //        targetRepository = (TargetRepository) applicationContext.getBean("targetRepository");
         Optional<Target> target = targetRepository.findByKey(obj.toString());
         if (target.isPresent())
            return false;
      }
      return true;
   }
}
