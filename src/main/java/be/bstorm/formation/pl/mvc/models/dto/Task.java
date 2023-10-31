package be.bstorm.formation.pl.mvc.models.dto;

import be.bstorm.formation.dal.models.entities.Status;
import be.bstorm.formation.dal.models.entities.TaskEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record Task (
    Long id,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate date,
    String name,
    String description,
    Status status,
    Long taskListId
    ){
    
    public static Task toDTO(TaskEntity entity){
        return new Task(entity.getId(),entity.getDate(), entity.getName(), entity.getDescription(), entity.getStatus(), entity.getTaskListEntity().getId());
    }
}
