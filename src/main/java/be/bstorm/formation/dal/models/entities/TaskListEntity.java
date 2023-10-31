package be.bstorm.formation.dal.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "taskList")
@Data
public class TaskListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private LocalDate creation;
    private LocalDate lastUpdated;

    @ManyToMany
    @JoinTable(name = "taskList_userEntities",
            joinColumns = @JoinColumn(name = "taskListEntity_id"),
            inverseJoinColumns = @JoinColumn(name = "viewers_id"))
    private Set<UserEntity> viewersEntities = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "owner_entity_id")
    private UserEntity ownerEntity;

    @OneToMany(mappedBy = "taskListEntity", orphanRemoval = true)
    private List<TaskEntity> taskEntities = new ArrayList<>();

}
