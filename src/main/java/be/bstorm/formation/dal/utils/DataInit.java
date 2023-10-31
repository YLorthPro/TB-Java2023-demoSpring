package be.bstorm.formation.dal.utils;


import be.bstorm.formation.dal.models.entities.*;
import be.bstorm.formation.dal.models.enums.TaskStatus;
import be.bstorm.formation.dal.models.enums.UserRole;
import be.bstorm.formation.dal.repository.TaskListRepository;
import be.bstorm.formation.dal.repository.TaskRepository;
import be.bstorm.formation.dal.repository.UserRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInit implements InitializingBean {
    
    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;
    private final UserRepository userRepository;
    

    public DataInit(TaskRepository taskRepository, TaskListRepository taskListRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
        this.userRepository = userRepository;
    }


    @Override
    public void afterPropertiesSet() {

        UserEntity moi = new UserEntity();
        moi.setBirthdate(LocalDate.of(1991,03,13));
        moi.setRole(UserRole.ADMIN);
        moi.setLogin("yann");
        moi.setPassword("Test1234=");
        moi.setFirstName("Yann");
        moi.setLastName("Lorthioir");

        userRepository.save(moi);

        TaskListEntity listEntity = new TaskListEntity();
        listEntity.setName("Coucou les javas");
        listEntity.setCreation(LocalDate.now());
        listEntity.setLastUpdated(LocalDate.now());
        listEntity.setOwnerEntity(moi);
        taskListRepository.save(listEntity);
        
        TaskEntity tache = new TaskEntity();
        tache.setName("test");
        tache.setDescription("retest");
        tache.setDate(LocalDate.now());
        tache.setStatus(TaskStatus.PENDING);
        tache.setTaskListEntity(listEntity);
        taskRepository.save(tache);


        
    }
}
