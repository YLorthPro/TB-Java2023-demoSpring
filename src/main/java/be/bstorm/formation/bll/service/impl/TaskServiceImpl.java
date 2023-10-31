package be.bstorm.formation.bll.service.impl;

import be.bstorm.formation.bll.service.TaskService;
import be.bstorm.formation.dal.models.entities.Status;
import be.bstorm.formation.dal.models.entities.TaskEntity;
import be.bstorm.formation.dal.models.entities.UserEntity;
import be.bstorm.formation.bll.models.exception.NotFoundException;
import be.bstorm.formation.pl.mvc.models.forms.TaskForm;
import be.bstorm.formation.dal.repository.TaskListRepository;
import be.bstorm.formation.dal.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    
    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;

    public TaskServiceImpl(TaskRepository taskRepository,
                           TaskListRepository taskListRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
    }

    @Override
    public Set<TaskEntity> getAll(String login) {
        return new HashSet<>(taskRepository.findAll());
    }

    @Override
    public Optional<TaskEntity> getOne(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public void create(TaskForm form) {
        if(form == null)
            throw new RuntimeException("Form can't be null");

        TaskEntity entity = new TaskEntity();
        entity.setName(form.getName());
        entity.setDate(form.getDate());
        entity.setDescription(form.getDescription());
        entity.setStatus(form.getStatus());
        entity.setTaskListEntity(taskListRepository.findById(form.getTaskListId()).orElseThrow(() -> new NotFoundException("TaskList not found")));
        
        taskRepository.save(entity);
    }

    @Override
    public void update(Long id, TaskForm form) {
        if(form == null)
            throw new RuntimeException("Form can't be null");

        TaskEntity entity = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));
        entity.setName(form.getName());
        entity.setDate(form.getDate());
        entity.setDescription(form.getDescription());
        entity.setStatus(form.getStatus());

        taskRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        taskRepository.delete(taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found")));
    }

    @Override
    public void complete(Long id) {
        TaskEntity entity = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));
        entity.setStatus(Status.DONE);
        taskRepository.save(entity);
    }

    @Override
    public Set<TaskEntity> getAllByStatus(String login, Status status) {
        return taskRepository.findAllByStatus(status).stream()
                .filter(taskEntity -> taskEntity.getTaskListEntity().getOwnerEntity().getLogin().equals(login)||
                        taskEntity.getTaskListEntity().getViewersEntities().stream()
                                .map(UserEntity::getLogin)
                                .collect(Collectors.toSet())
                                .equals(login))
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteAllWhereComplete() {
        Set<TaskEntity> tasksCompleted = taskRepository.findAllByStatus(Status.DONE);
        taskRepository.deleteAll(tasksCompleted);
    }
}
