package be.bstorm.formation.dal.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "task")
@Data
public class TaskEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    @Enumerated(value = EnumType.ORDINAL)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "task_list_entity_id")
    private TaskListEntity taskListEntity;
    

}

