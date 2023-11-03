package be.bstorm.formation.bll.service.impl;

import be.bstorm.formation.bll.service.TaskListService;
import be.bstorm.formation.dal.models.entities.TaskListEntity;
import be.bstorm.formation.dal.models.entities.UserEntity;
import be.bstorm.formation.bll.models.exception.NotFoundException;
import be.bstorm.formation.dal.repository.TaskListRepository;
import be.bstorm.formation.dal.repository.UserRepository;
import be.bstorm.formation.pl.models.forms.MVCTaskListForm;
import be.bstorm.formation.pl.models.forms.RestTaskListForm;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

/**
 * Injection de dépendance: 3 pré-requis
 * - 1 Annotation sur la classe dont le bean va être créé (ici TaskListRepository et UserRepository)
 * - 2 Annotation sur la classe qui va utiliser le bean (ici TaskListServiceImpl)
 * - 3 Constructeur ayant en paramètre la classe dont le bean va être créé
 */
@Service
public class TaskListServiceImpl implements TaskListService {
    
    private final TaskListRepository taskListRepository;
    private final UserRepository userRepository;

    public TaskListServiceImpl(TaskListRepository taskListRepository,
                               UserRepository userRepository) {
        this.taskListRepository = taskListRepository;
        this.userRepository = userRepository;
    }

    /**
     * Récupère l'ensemble des catégories (TaskList) associées à un utilisateur spécifié.
     *
     * Cette méthode recherche toutes les TaskList appartenant à l'utilisateur
     * avec le login donné, ainsi que toutes les TaskList auxquelles l'utilisateur
     * a accès en tant que viewer.
     *
     * @param login Le nom d'utilisateur de l'utilisateur pour lequel les TaskList
     *             doivent être récupérées.
     * @return Un Set contenant toutes les entités de TaskList
     *         à l'utilisateur spécifié, y compris celles qu'il possède et celles auxquelles
     *         il a accès en tant que viewer.
     * @throws NotFoundException Si aucun utilisateur correspondant au login n'est trouvé,
     *                          une exception NotFoundException est levée.
     */
    @Override
    public Set<TaskListEntity> getAll(String login) {
        UserEntity user = userRepository.findByLogin(login).orElseThrow(()->new NotFoundException("Pas trouvé"));
        
        return taskListRepository.findAllByUserEntity(user);
    }

    /**
     * Récupère l'ensemble des TaskList appartenant à un utilisateur spécifié en tant que propriétaire.
     *
     * Cette méthode recherche toutes les TaskList qui sont la propriété de l'utilisateur
     * avec le login donné.
     *
     * @param login Le nom d'utilisateur de l'utilisateur propriétaire des TaskList
     *             à récupérer.
     * @return Un Set contenant toutes les entités TaskList appartenant à
     *         l'utilisateur spécifié en tant que propriétaire.
     * @throws NotFoundException Si aucun utilisateur correspondant au login n'est trouvé,
     *                          une exception NotFoundException est levée.
     */
    @Override
    public Set<TaskListEntity> getAllAsOwner(String login) {
        UserEntity user = userRepository.findByLogin(login).orElseThrow(()->new NotFoundException("Pas trouvé"));
        
        return taskListRepository.findAllByUserEntity(user);
    }

    /**
     * Récupère une TaskList à partir de son id (Heidi).
     *
     * Cette méthode recherche une TaskList en utilisant l'id.
     *
     * @param id L'identifiant unique de TaskList à récupérer.
     * @return Un Optional contenant l'entité de la TaskList correspondant
     *         à l'id donné, s'il existe. Si aucune TaskList correspondante n'est
     *         trouvée, l'Optional sera vide.
     */
    @Override
    public Optional<TaskListEntity> getOne(Long id) {
        return taskListRepository.findById(id);
    }

    /**
     * Crée une nouvelle TaskList en utilisant les informations fournies dans le formulaire.
     *
     * Cette méthode crée une nouvelle TaskList en utilisant les données du formulaire. Elle attribue la date de création, la date de dernière mise à jour et l'utilisateur
     * propriétaire de la liste de tâches en fonction des informations du formulaire.
     *
     * @param form Le formulaire contenant les informations nécessaires pour créer la nouvelle TaskList.
     *            Il ne doit pas être null.
     * @throws RuntimeException Si le formulaire est null, une exception de type RuntimeException est
     *                          levée.
     * @throws NotFoundException Si l'utilisateur propriétaire spécifié dans le formulaire n'est pas
     *                          trouvé, une exception NotFoundException est levée pour indiquer que
     *                          l'utilisateur n'a pas été trouvé.
     */
    @Override
    public void create(RestTaskListForm form, String login) {
        if(form == null)
            throw new IllegalArgumentException("Form can't be null");

        TaskListEntity entity = new TaskListEntity();
        entity.setName(form.getName());
        entity.setCreation(LocalDate.now());
        entity.setLastUpdated(LocalDate.now());
        if(form instanceof MVCTaskListForm)
            entity.setOwnerEntity(userRepository.findByLogin(((MVCTaskListForm) form).getOwner()).orElseThrow(() -> new NotFoundException("Pas trouvé non plus")));
        else
            entity.setOwnerEntity(userRepository.findByLogin(login).orElseThrow(() -> new NotFoundException("Pas trouvé non plus")));
        entity.setViewersEntities(form.getViewersEntitiesLogin().stream().map(ve-> userRepository.findByLogin(ve).get()).toList());
        
        taskListRepository.save(entity);
    }

    /**
     * Met à jour une TaskList existante en utilisant les informations fournies dans le formulaire.
     *
     * Cette méthode recherche la TaskList existante à partir de son id, puis
     * met à jour ses données en utilisant les informations du formulaire. Elle met à jour le
     * nom de la TaskList et la date de dernière mise à jour.
     *
     * @param id L'id de la TaskList à mettre à jour.
     * @param form Le formulaire contenant les informations nécessaires pour mettre à jour la TaskList. 
     *             Il ne doit pas être null.
     * @throws RuntimeException Si le formulaire est null, une exception de type RuntimeException est levée.
     * @throws NotFoundException Si la liste de tâches correspondant à l'identifiant spécifié n'est pas
     *                          trouvée, une exception NotFoundException est levée.
     */
    @Override
    public void update(Long id, RestTaskListForm form) {
        if(form == null)
            throw new IllegalArgumentException("Form can't be null");

        TaskListEntity entity = taskListRepository.findById(id).orElseThrow(()->new NotFoundException("Pas trouvé"));
        entity.setName(form.getName());
        entity.setLastUpdated(LocalDate.now());
        entity.getViewersEntities().clear();
        entity.getViewersEntities().addAll(form.getViewersEntitiesLogin().stream()
                .map(ve->userRepository.findByLogin(ve).get())
                .toList());


        taskListRepository.save(entity);
    }

    /**
     * Supprime une TaskList en fonction de son id.
     *
     * Cette méthode supprime la TaskList ayant l'id spécifié. Si la TaskList
     * correspondante est trouvée, elle sera supprimée.
     *
     * @param id L'identifiant unique de la TaskList à supprimer.
     */
    @Override
    public void delete(Long id) {
        taskListRepository.deleteById(id);
    }
}
