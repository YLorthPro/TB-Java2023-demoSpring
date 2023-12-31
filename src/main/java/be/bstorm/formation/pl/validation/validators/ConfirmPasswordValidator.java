package be.bstorm.formation.pl.validation.validators;

import be.bstorm.formation.pl.models.forms.RegisterForm;
import be.bstorm.formation.pl.validation.constraints.ConfirmPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, RegisterForm> {

    @Override
    public boolean isValid(RegisterForm value, ConstraintValidatorContext context) {
        return value.getPassword().equals(value.getConfirmPassword());
    }
}
