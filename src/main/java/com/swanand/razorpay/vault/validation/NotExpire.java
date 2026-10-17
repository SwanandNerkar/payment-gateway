package com.swanand.razorpay.vault.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { })
public @interface NotExpire {

    String message() default "Not Valid or Expired Month can't be Entered";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
