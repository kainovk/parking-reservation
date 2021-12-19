package ru.tinkoff.fintech.parking.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BookingValidator.class)
public @interface BookingConstraint {

    String message() default "Booking data is not valid!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
