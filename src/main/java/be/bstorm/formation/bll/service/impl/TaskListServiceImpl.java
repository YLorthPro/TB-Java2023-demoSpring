package be.bstorm.formation.bll.service.impl;

import be.bstorm.formation.bll.service.TaskListService;
import be.bstorm.formation.dal.models.entities.TaskListEntity;
import be.bstorm.formation.dal.models.entities.UserEntity;
import be.bstorm.formation.bll.models.exception.NotFoundException;
import be.bstorm.formation.pl.mvc.models.forms.TaskListForm;
import be.bstorm.formation.dal.repository.TaskListRepository;
import be.bstorm.formation.dal.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Service
public class TaskListServiceImpl implements TaskListService {
    
    private final TaskListRepository taskListRepository;
    private final UserRepository userRepository;

    public TaskListServiceImpl(TaskListRepository taskListRepository,
                               UserRepository userRepository) {
        this.taskListRepository = taskListRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Set<TaskListEntity> getAll(String login) {
        UserEntity user = userRepository.findByLogin(login).orElseThrow(()->new NotFoundException("Pas trouvé"));
        
        Set<TaskListEntity> all = taskListRepository.findAllByOwnerEntity(user);
        all.addAll(taskListRepository.findAllByViewersEntities(user));
        return all;
    }

    @Override
    public Set<TaskListEntity> getAllAsOwner(String login) {
        UserEntity user = userRepository.findByLogin(login).orElseThrow(()->new NotFoundException("Pas trouvé"));
        
        return taskListRepository.findAllByOwnerEntity(user);
    }

    @Override
    public Optional<TaskListEntity> getOne(Long id) {
        return taskListRepository.findById(id);
    }

    @Override
    public void create(TaskListForm form) {
        if(form == null)
            throw new RuntimeException("Form can't be null");

        TaskListEntity entity = new TaskListEntity();
        entity.setName(form.getName());
        entity.setCreation(LocalDate.now());
        entity.setLastUpdated(LocalDate.now());
        entity.setOwnerEntity(userRepository.findByLogin(form.getOwner()).orElseThrow(() -> new NotFoundException("Pas trouvé non plus")));

        taskListRepository.save(entity);
    }

    @Override
    public void update(Long id, TaskListForm form) {
        if(form == null)
            throw new RuntimeException("Form can't be null");

        TaskListEntity entity = taskListRepository.findById(id).orElseThrow(()->new NotFoundException("Pas trouvé"));
        entity.setName(form.getName());
        entity.setLastUpdated(LocalDate.now());

        taskListRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        taskListRepository.deleteById(id);
    }
}
