package be.bstorm.formation.validation.validators;


import be.bstorm.formation.validation.constraints.NotEquals;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotEqualsValidator implements ConstraintValidator<NotEquals, String> {

    private String notEqualValue;

    @Override
    public void initialize(NotEquals constraintAnnotation) {
        notEqualValue = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !value.equals(notEqualValue) ;
    }
}
