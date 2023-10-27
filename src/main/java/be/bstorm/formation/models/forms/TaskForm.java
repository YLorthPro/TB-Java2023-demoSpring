package be.bstorm.formation.models.forms;

import be.bstorm.formation.models.entities.Status;
import be.bstorm.formation.validation.constraints.InFuture;
import be.bstorm.formation.validation.constraints.NotEquals;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
public class TaskForm{
    private LocalDate date;
    @NotBlank
    @Size(min = 4)
    private String name;
    @NotEquals(message = "Pas coucou Gaetan", value = "bonjour")
    private String description;
    private Status status;
}
