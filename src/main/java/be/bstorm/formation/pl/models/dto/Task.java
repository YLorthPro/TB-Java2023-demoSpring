package be.bstorm.formation.pl.models.dto;

import be.bstorm.formation.dal.models.enums.TaskStatus;
import be.bstorm.formation.dal.models.entities.TaskEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record Task (
    Long id,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate date,
    String name,
    String description,
    TaskStatus status,
    Long taskListId
    ){
    
    public static Task toDTO(TaskEntity entity){
        return new Task(entity.getId(),entity.getDate(), entity.getName(), entity.getDescription(), entity.getStatus(), entity.getTaskListEntity().getId());
    }
}
