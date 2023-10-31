package be.bstorm.formation.pl.validation.constraints;

import be.bstorm.formation.pl.validation.validators.InFutureValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InFutureValidator.class)
public @interface InFuture {

    long amount() default 3;
    ChronoUnit unit() default ChronoUnit.DAYS;

    String message() default "should be 0 days in the future";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
