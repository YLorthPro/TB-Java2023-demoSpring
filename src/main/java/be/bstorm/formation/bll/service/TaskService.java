package be.bstorm.formation.bll.service;

import be.bstorm.formation.dal.models.enums.TaskStatus;
import be.bstorm.formation.dal.models.entities.TaskEntity;
import be.bstorm.formation.pl.models.forms.TaskForm;

import java.util.Optional;
import java.util.Set;

public interface TaskService {
    //CRUD
    Set<TaskEntity> getAll(String login);
    Optional<TaskEntity> getOne(Long id);
    void create(TaskForm form);
    void update(Long id, TaskForm form);
    void delete(Long id);
    
    // Marquer tache fini
    void complete(Long id);

    // All par status
    Set<TaskEntity> getAllByStatus(String login, TaskStatus status);
    
    // Delete complete
    void deleteAllWhereComplete();
    
}
