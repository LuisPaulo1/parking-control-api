package com.api.parkingcontrol.services.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = ParkingSpotValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ParkingSpotValid {

	String message() default "Error de validação";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
