package be.bstorm.formation.dal.repository;

import be.bstorm.formation.dal.models.entities.TaskListEntity;
import be.bstorm.formation.dal.models.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface TaskListRepository extends JpaRepository<TaskListEntity, Long> {
    Set<TaskListEntity> findAllByOwnerEntity(UserEntity owner);

    @Query("SELECT t FROM TaskListEntity t JOIN t.viewersEntities v WHERE :viewerEntity MEMBER OF t.viewersEntities")
    Set<TaskListEntity> findAllByViewerEntity(@Param("viewerEntity") UserEntity viewer);

    @Query("SELECT DISTINCT t FROM TaskListEntity t " +
            "LEFT JOIN t.viewersEntities v " +
            "WHERE t.ownerEntity = :userEntity OR :userEntity MEMBER OF t.viewersEntities")
    Set<TaskListEntity> findAllByUserEntity(@Param("userEntity") UserEntity viewer);
}