package be.bstorm.formation.bll.service;

import be.bstorm.formation.dal.models.entities.TaskListEntity;
import be.bstorm.formation.pl.models.forms.TaskListForm;

import java.util.Optional;
import java.util.Set;

public interface TaskListService {
    Set<TaskListEntity> getAll(String login);
    Set<TaskListEntity> getAllAsOwner(String login);
    Optional<TaskListEntity> getOne(Long id);
    void create(TaskListForm entity);
    void update(Long id, TaskListForm form);
    void delete(Long id);
}