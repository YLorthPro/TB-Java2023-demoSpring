package be.bstorm.formation.models.dto;

import be.bstorm.formation.models.entities.Status;
import be.bstorm.formation.models.entities.TaskEntity;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Builder;

import java.time.LocalDate;

public record Task (
    Long id,
    LocalDate date,
    String name,
    String description,
    Status status
    ){
    
    public static Task toDTO(TaskEntity entity){
        return new Task(entity.getId(),entity.getDate(), entity.getName(), entity.getDescription(), entity.getStatus());
    }
}
