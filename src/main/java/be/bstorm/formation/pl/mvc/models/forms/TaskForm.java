package be.bstorm.formation.pl.mvc.models.forms;

import be.bstorm.formation.dal.models.entities.Status;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class TaskForm{
    @FutureOrPresent
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @NotBlank
    @Size(min = 4)
    private String name;
    @NotBlank
    @Size(min = 4)
    private String description;
    private Status status;
    private Long taskListId;
}