package be.bstorm.formation.pl.mvc.models.dto;

import be.bstorm.formation.dal.models.entities.TaskListEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public record TaskList(
    Long id,
    String name,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate creation,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate lastUpdated,

    Set<Task> taskEntities,
    Set<User> viewersEntities,
    User owner
    ){

    public static TaskList toDTO(TaskListEntity entity){
        return new TaskList(entity.getId(),entity.getName(), entity.getCreation(), entity.getLastUpdated(), entity.getTaskEntities().stream().map(Task::toDTO).collect(Collectors.toSet()), entity.getViewersEntities().stream().map(User::toDTO).collect(Collectors.toSet()), User.toDTO(entity.getOwnerEntity()));
    }
}
