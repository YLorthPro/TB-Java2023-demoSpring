package be.bstorm.formation.service;

import be.bstorm.formation.models.entities.Status;
import be.bstorm.formation.models.entities.TaskEntity;
import be.bstorm.formation.models.forms.TaskForm;

import java.util.Optional;
import java.util.Set;

public interface TaskService {
    //CRUD
    Set<TaskEntity> getAll();
    Optional<TaskEntity> getOne(Long id);
    void create(TaskForm form);
    void update(Long id, TaskForm form);
    void delete(Long id);
    
    // Marquer tache fini
    void complete(Long id);

    // All par status
    Set<TaskEntity> getAllByStatus(Status status);
    
    // Delete complete
    void deleteAllWhereComplete();
    
}
