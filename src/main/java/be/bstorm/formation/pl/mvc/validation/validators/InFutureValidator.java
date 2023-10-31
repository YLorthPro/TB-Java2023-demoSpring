package be.bstorm.formation.pl.mvc.validation.validators;

import be.bstorm.formation.pl.mvc.validation.constraints.InFuture;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;

public class InFutureValidator implements ConstraintValidator<InFuture, Temporal> {

    private InFuture annotation;

    @Override
    public void initialize(InFuture constraintAnnotation) {
        annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Temporal value, ConstraintValidatorContext context) {

        if(value == null)
            return true;

        context.disableDefaultConstraintViolation();

        if(
                (value instanceof LocalDate && !checkLocalDateValid((LocalDate)value) ) ||
                (value instanceof LocalDateTime && !checkLocalDateTime((LocalDateTime)value) )
        ){
            context.buildConstraintViolationWithTemplate("Should be "+annotation.amount()+" "+annotation.unit().name().toLowerCase()+" in the future" )
                    .addConstraintViolation();
            return false;
        }
        else if( !(value instanceof LocalDate) && !(value instanceof LocalDateTime)  ) {
            throw  new IllegalArgumentException();
        }
        else
            return true;
    }

    private boolean checkLocalDateValid(LocalDate localDate){
        return LocalDate.now()
                .plus( annotation.amount(), annotation.unit() )
                .isBefore( localDate );
    }

    private boolean checkLocalDateTime(LocalDateTime localDateTime){
        return LocalDateTime.now()
                .plus( annotation.amount(), annotation.unit() )
                .isBefore( localDateTime );
    }
}
