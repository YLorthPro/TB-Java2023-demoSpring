package be.bstorm.formation.pl.models.dto;

import be.bstorm.formation.dal.models.entities.TaskListEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Data
public class TaskList {
    private Long id;
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate creation;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastUpdated;

    private Set<Task> taskEntities;
    private Set<String> viewersEntitiesLogin;
    private String owner;

    public static TaskList toDTO(TaskListEntity entity) {
        return new TaskList(
                entity.getId(),
                entity.getName(),
                entity.getCreation(),
                entity.getLastUpdated(),
                entity.getTaskEntities().stream().map(Task::toDTO).collect(Collectors.toSet()),
                entity.getViewersEntities().stream().map(User::toDTO).map(User::login).collect(Collectors.toSet()),
                entity.getOwnerEntity().getLogin()
        );
    }
}
