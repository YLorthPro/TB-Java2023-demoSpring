package be.bstorm.formation.bll.service;

import be.bstorm.formation.dal.models.entities.TaskListEntity;
import be.bstorm.formation.pl.models.forms.RestTaskListForm;

import java.util.Optional;
import java.util.Set;

public interface TaskListService {
    Set<TaskListEntity> getAll(String login);
    Set<TaskListEntity> getAllAsOwner(String login);
    Optional<TaskListEntity> getOne(Long id);
    void create(RestTaskListForm entity, String login);
    void update(Long id, RestTaskListForm form);
    void delete(Long id);
}