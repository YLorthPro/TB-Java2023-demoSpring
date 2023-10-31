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

    /**
    * @ManyToMany une catégorie(TaskList) peut avoir plusieurs viewers (user qui ne pourront que lire la catégorie) 
    * et un utilisateur peut avoir plusieurs catégories dont il peut lire les infos
    * 
    * @JoinTable comme c'est une many to many, il doit y avoir une table de jointure.
    * Cette table sera donc créée et gérée par JPA automatiquement
    * 
    * @JoinColumn dans la table de jointure, il y aura 2 clés composites 
    * (correspondant aux deux clés primaires des tables User et TaskList)
    * la première (joinColumns) faisant référence à l'id de TaskList et la deuxième (inverseJoinColumns)
    * faisant référence à l'id de l'autre table de la relation (User)
    * 
    * Il en résulte donc une List contenant les User qui ne pourront que voir la TaskList
    */
    @ManyToMany
    @JoinTable(name = "taskList_userEntities",
            joinColumns = @JoinColumn(name = "taskListEntity_id"),
            inverseJoinColumns = @JoinColumn(name = "viewers_id"))
    private List<UserEntity> viewersEntities = new ArrayList<>();

    /**
     * @ManyToOne une catégorie n'a qu'un seul propriétaire (owner)
     * cependant, un User peut être propriétaire de plusieurs catégories
     *
     * @JoinColumn la colonne de jointure comprendra l'id du propriétaire de la catégorie qui aura comme nom "owner_entity_id"
     */
    @ManyToOne
    @JoinColumn(name = "owner_entity_id")
    private UserEntity ownerEntity;

    /**
     * @OneToMany une catégorie possède plusieurs tâches (Task)
     * cependant, une tâche n'appartient qu'à une seule TaskList
     *
     * mappedBy indique le nom de l'attribut dans l'entité cible (celle qui possède la relation "ManyToOne", Task ici)
     * qui maintient la référence vers l'entité annotée (dans ce cas, "taskListEntity").
     * Cela signifie que l'entité cible a un champ nommé "taskListEntity" qui est utilisé pour gérer la relation entre les deux entités
     * 
     * orphanRemoval signifie que lorsqu'on supprime une référence d'une instance de l'entité annotée
     * à une instance de l'entité cible, l'instance de l'entité cible elle-même doit être supprimée 
     * si elle n'est plus associée à aucune autre instance de l'entité annotée
     */
    @OneToMany(mappedBy = "taskListEntity", orphanRemoval = true)
    private List<TaskEntity> taskEntities = new ArrayList<>();

}
