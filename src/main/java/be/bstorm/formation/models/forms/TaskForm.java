package be.bstorm.formation.models.forms;

import be.bstorm.formation.models.entities.Status;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskForm{
    @FutureOrPresent
    @NotNull(message = "Pas null coco")
    private LocalDate date;
    @NotBlank
    @Size(min = 4)
    private String name;
    private String description;
    private Status status;
}
