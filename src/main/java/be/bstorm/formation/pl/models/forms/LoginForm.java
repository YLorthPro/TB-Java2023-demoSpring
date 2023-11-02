package be.bstorm.formation.pl.models.forms;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginForm {
    @NotNull
    @Size(max = 40)
    private String login;
    @NotNull
    @Size(min = 4, max = 40)
    private String password;
}
