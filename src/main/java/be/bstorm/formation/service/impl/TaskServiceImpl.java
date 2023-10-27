package be.bstorm.formation.service.impl;

import be.bstorm.formation.models.entities.Status;
import be.bstorm.formation.models.entities.TaskEntity;
import be.bstorm.formation.models.forms.TaskForm;
import be.bstorm.formation.repository.TaskRepository;
import be.bstorm.formation.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class TaskServiceImpl implements TaskService {
    
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Set<TaskEntity> getAll() {
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
        
        taskRepository.save(entity);
    }

    @Override
    public void update(Long id, TaskForm form) {
        if(form == null)
            throw new RuntimeException("Form can't be null");

        TaskEntity entity = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        entity.setName(form.getName());
        entity.setDate(form.getDate());
        entity.setDescription(form.getDescription());
        entity.setStatus(form.getStatus());

        taskRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        taskRepository.delete(taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found")));
    }

    @Override
    public void complete(Long id) {
        TaskEntity entity = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        entity.setStatus(Status.DONE);
        taskRepository.save(entity);
    }

    @Override
    public Set<TaskEntity> getAllByStatus(Status status) {
        return new HashSet<>(taskRepository.findAllByStatus(status));
    }

    @Override
    public void deleteAllWhereComplete() {
        Set<TaskEntity> tasksCompleted = taskRepository.findAllByStatus(Status.DONE);
        taskRepository.deleteAll(tasksCompleted);
    }
}
