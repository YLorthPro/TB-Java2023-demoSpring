package be.bstorm.formation.bll.service.impl;

import be.bstorm.formation.bll.service.TaskService;
import be.bstorm.formation.dal.models.enums.TaskStatus;
import be.bstorm.formation.dal.models.entities.TaskEntity;
import be.bstorm.formation.dal.models.entities.UserEntity;
import be.bstorm.formation.bll.models.exception.NotFoundException;
import be.bstorm.formation.pl.models.forms.TaskForm;
import be.bstorm.formation.dal.repository.TaskListRepository;
import be.bstorm.formation.dal.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    
    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;

    public TaskServiceImpl(TaskRepository taskRepository,
                           TaskListRepository taskListRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
    }

    /**
     * Récupère l'ensemble des tâches appartenant à un utilisateur spécifié.
     *
     * Cette méthode filtre toutes les tâches disponibles dans le système pour ne conserver que celles
     * qui sont liées à l'utilisateur identifié par le login. Les tâches incluses dans l'ensemble de
     * résultat appartiennent à des listes de tâches dont le propriétaire est l'utilisateur spécifié.
     *
     * @param login Le nom d'utilisateur de l'utilisateur pour lequel les tâches doivent être récupérées.
     * @return Un Set contenant toutes les entités de tâches appartenant à l'utilisateur
     *         spécifié en tant que propriétaire.
     */
    @Override
    public Set<TaskEntity> getAll(String login) {
        return taskRepository.findAll().stream()
                .filter(taskEntity -> taskEntity.getTaskListEntity().getOwnerEntity().getLogin().equals(login))
                .collect(Collectors.toSet());
    }

    /**
     * Récupère une tâche spécifique à partir de son id.
     *
     * Cette méthode recherche une tâche en utilisant l'identifiant unique spécifié. Si une tâche
     * correspondante est trouvée, elle est renvoyée dans Optional.
     *
     * @param id L'identifiant de la tâche à récupérer.
     * @return Un Optional contenant l'entité de la tâche correspondant à
     *         l'identifiant donné, s'il existe. Si aucune tâche correspondante n'est trouvée,
     *         l'Optional sera vide.
     */
    @Override
    public Optional<TaskEntity> getOne(Long id) {
        return taskRepository.findById(id);
    }

    /**
     * Crée une nouvelle tâche en utilisant les informations fournies dans le formulaire.
     *
     * Cette méthode crée une nouvelle tâche en utilisant les données du formulaire. Elle attribue
     * le nom, la date, la description, le statut et la liste de tâches associée à la tâche en fonction des
     * informations du formulaire.
     *
     * @param form Le formulaire contenant les informations nécessaires pour créer la nouvelle tâche. Il ne
     *             doit pas être null.
     * @throws IllegalArgumentException Si le formulaire est null ou s'il contient des données invalides, une
     *                                  exception de type IllegalArgumentException est levée pour indiquer que
     *                                  les données sont invalides.
     * @throws NotFoundException Si la liste de tâches associée à la tâche spécifiée dans le formulaire n'est
     *                          pas trouvée, une exception NotFoundException est levée.
     */
    @Override
    public void create(TaskForm form) {
        if(form == null)
            throw new IllegalArgumentException("Form can't be null");

        TaskEntity entity = new TaskEntity();
        entity.setName(form.getName());
        entity.setDate(form.getDate());
        entity.setDescription(form.getDescription());
        entity.setStatus(form.getStatus());
        entity.setTaskListEntity(taskListRepository.findById(form.getTaskListId()).orElseThrow(() -> new NotFoundException("TaskList not found")));
        
        taskRepository.save(entity);
    }

    /**
     * Met à jour une tâche existante en utilisant les informations fournies dans le formulaire.
     *
     * Cette méthode recherche la tâche existante à partir de son id, puis met à jour ses
     * données en utilisant les informations du formulaire. Elle met à jour le nom, la date, la
     * description et le statut de la tâche.
     *
     * @param id L'identifiant unique de la tâche à mettre à jour.
     * @param form Le formulaire contenant les informations nécessaires pour mettre à jour la tâche. Il ne
     *             doit pas être null.
     * @throws IllegalArgumentException Si le formulaire est null ou s'il contient des données invalides, une
     *                                  exception de type IllegalArgumentException est levée.
     * @throws NotFoundException Si la tâche correspondant à l'identifiant spécifié n'est pas trouvée, une
     *                          exception NotFoundException est levée.
     */
    @Override
    public void update(Long id, TaskForm form) {
        if(form == null)
            throw new IllegalArgumentException("Form can't be null");

        TaskEntity entity = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));
        entity.setName(form.getName());
        entity.setDate(form.getDate());
        entity.setDescription(form.getDescription());
        entity.setStatus(form.getStatus());

        taskRepository.save(entity);
    }

    /**
     * Supprime une tâche existante en utilisant son id.
     *
     * Cette méthode recherche la tâche existante à partir de son id, puis la supprime de la base de données.
     *
     * @param id L'identifiant unique de la tâche à supprimer.
     * @throws NotFoundException Si la tâche correspondant à l'identifiant spécifié n'est pas trouvée, une exception
     *                          NotFoundException est levée.
     */
    @Override
    public void delete(Long id) {
        taskRepository.delete(taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found")));
    }

    /**
     * Marque une tâche spécifique comme complète en utilisant son id.
     *
     * Cette méthode recherche la tâche spécifique à partir de son id, puis la marque comme
     * complète en définissant son statut sur "DONE".
     *
     * @param id L'identifiant unique de la tâche à marquer comme complète.
     * @throws NotFoundException Si la tâche correspondant à l'identifiant spécifié n'est pas trouvée, une exception
     *                          NotFoundException est levée.
     */
    @Override
    public void complete(Long id) {
        TaskEntity entity = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));
        entity.setStatus(TaskStatus.DONE);
        taskRepository.save(entity);
    }

    /**
     * Récupère l'ensemble des tâches avec un statut spécifié pour un utilisateur spécifié.
     *
     * Cette méthode retourne un Set contenant toutes les entités de tâches qui ont le statut
     * spécifié et qui sont associées à l'utilisateur identifié par le login. Les tâches incluses dans cet
     * ensemble de résultat appartiennent aux listes de tâches dont le propriétaire est l'utilisateur spécifié
     * ou auxquelles l'utilisateur a accès en tant que viewer.
     *
     * @param login Le nom d'utilisateur de l'utilisateur pour lequel les tâches doivent être récupérées.
     * @param status Le statut des tâches à récupérer (par exemple, TaskStatus.DONE).
     * @return Un Set contenant toutes les entités de tâches ayant le statut spécifié et associées
     *         à l'utilisateur spécifié en tant que propriétaire ou viewer.
     */
    @Override
    public Set<TaskEntity> getAllByStatus(String login, TaskStatus status) {
        return taskRepository.findAllByStatus(status).stream()
                .filter(taskEntity -> taskEntity.getTaskListEntity().getOwnerEntity().getLogin().equals(login)||
                        (taskEntity.getTaskListEntity().getViewersEntities().stream()
                                .map(UserEntity::getLogin)
                                .collect(Collectors.toSet())
                                .equals(login)))
                .collect(Collectors.toSet());
    }

    /**
     * Supprime toutes les tâches complétées.
     *
     * Cette méthode recherche toutes les tâches ayant le statut "DONE" (complétées) et les supprime
     * de la base de données.
     */
    @Override
    public void deleteAllWhereComplete() {
        Set<TaskEntity> tasksCompleted = taskRepository.findAllByStatus(TaskStatus.DONE);
        taskRepository.deleteAll(tasksCompleted);
    }
}
