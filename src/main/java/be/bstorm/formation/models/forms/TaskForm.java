package be.bstorm.formation.models.forms;

import be.bstorm.formation.models.entities.Status;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskForm{
    @FutureOrPresent
    @NotNull
    private LocalDate date;
    private String name;
    private String description;
    private Status status;
}
