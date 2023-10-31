package be.bstorm.formation.pl.mvc.models.forms;

import be.bstorm.formation.pl.validation.constraints.ConfirmPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@ConfirmPassword
public class RegisterForm {
    @NotBlank
    private String lastName;
    @NotBlank
    private String firstName;
    @NotNull
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
    @NotNull
    private String login;
    @NotNull
    private String password;
    private String confirmPassword;
}
