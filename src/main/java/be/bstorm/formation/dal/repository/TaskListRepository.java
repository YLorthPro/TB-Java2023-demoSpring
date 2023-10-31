package be.bstorm.formation.dal.repository;

import be.bstorm.formation.dal.models.entities.TaskListEntity;
import be.bstorm.formation.dal.models.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TaskListRepository extends JpaRepository<TaskListEntity, Long> {
    Set<TaskListEntity> findAllByOwnerEntity(UserEntity owner);
    
    //TODO custom query
    Set<TaskListEntity> findAllByViewersEntities(UserEntity viewer);
}