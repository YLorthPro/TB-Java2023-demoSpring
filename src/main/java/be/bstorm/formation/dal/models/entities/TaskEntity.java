package be.bstorm.formation.dal.models.entities;

import be.bstorm.formation.dal.models.enums.TaskStatus;
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
    private TaskStatus status;

    /**
     * @ManyToOne une tâche (Task) n'appartient qu'à une seule catégorie (TaskList)
     * cependant, une catégorie peut posséder plusieurs tâches
     *
     * @JoinColumn la colonne de jointure comprendra l'id de la catégorie qui aura comme nom "task_list_entity_id"
     */
    @ManyToOne
    @JoinColumn(name = "task_list_entity_id")
    private TaskListEntity taskListEntity;
    

}

