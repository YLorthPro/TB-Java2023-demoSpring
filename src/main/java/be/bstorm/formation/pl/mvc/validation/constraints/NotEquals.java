package be.bstorm.formation.pl.mvc.validation.constraints;


import be.bstorm.formation.pl.mvc.validation.validators.NotEqualsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotEqualsValidator.class)
public @interface NotEquals {

    String value() default "coucou";

    String message() default "value can't be equal to a secret word";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
