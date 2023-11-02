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
import java.util.ArrayList;

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

        UserEntity pasMoi = new UserEntity();
        pasMoi.setBirthdate(LocalDate.of(1991,03,14));
        pasMoi.setRole(UserRole.VISITOR);
        pasMoi.setLogin("pas yann");
        pasMoi.setPassword("Test1234=");
        pasMoi.setFirstName("pas Yann");
        pasMoi.setLastName("pas Lorthioir");

        userRepository.save(pasMoi);

        TaskListEntity listEntity = new TaskListEntity();
        listEntity.setName("Coucou les javas");
        listEntity.setCreation(LocalDate.now());
        listEntity.setLastUpdated(LocalDate.now());
        listEntity.setOwnerEntity(moi);
        ArrayList<UserEntity> liste = new ArrayList<>();
        liste.add(pasMoi);
        listEntity.setViewersEntities(liste);
        taskListRepository.save(listEntity);
        
        TaskEntity tache = new TaskEntity();
        tache.setName("test");
        tache.setDescription("retest");
        tache.setDate(LocalDate.now());
        tache.setStatus(TaskStatus.PENDING);
        tache.setTaskListEntity(listEntity);
        taskRepository.save(tache);

        TaskListEntity listEntity2 = new TaskListEntity();
        listEntity2.setName("Coucou les javas 2");
        listEntity2.setCreation(LocalDate.now());
        listEntity2.setLastUpdated(LocalDate.now());
        listEntity2.setOwnerEntity(pasMoi);
        taskListRepository.save(listEntity2);


        
    }
}
