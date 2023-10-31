package be.bstorm.formation.dal.repository;

import be.bstorm.formation.dal.models.enums.TaskStatus;
import be.bstorm.formation.dal.models.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    Set<TaskEntity> findAllByStatus(TaskStatus status);
}