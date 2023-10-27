package be.bstorm.formation.utils;


import be.bstorm.formation.models.entities.Status;
import be.bstorm.formation.models.entities.TaskEntity;
import be.bstorm.formation.repository.TaskRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInit implements InitializingBean {
    
    private final TaskRepository taskRepository;

    public DataInit(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        TaskEntity tache = new TaskEntity();
        tache.setName("test");
        tache.setDescription("retest");
        tache.setDate(LocalDate.now());
        tache.setStatus(Status.PENDING);
        
        taskRepository.save(tache);
    }
}
