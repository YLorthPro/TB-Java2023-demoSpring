package be.bstorm.formation.repository;

import be.bstorm.formation.models.entities.Status;
import be.bstorm.formation.models.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.Set;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    Set<TaskEntity> findAllByStatus(Status status);
    
}