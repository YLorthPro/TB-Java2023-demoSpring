package be.bstorm.formation.pl.mvc.controller;

import be.bstorm.formation.pl.mvc.models.dto.Task;
import be.bstorm.formation.pl.mvc.models.forms.TaskForm;
import be.bstorm.formation.dal.models.enums.TaskStatus;
import be.bstorm.formation.bll.models.exception.NotFoundException;
import be.bstorm.formation.bll.service.TaskListService;
import be.bstorm.formation.bll.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;


@Controller
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;
    private final TaskListService taskListService;
    public TaskController(TaskService taskService, TaskListService taskListService) {
        this.taskService = taskService;
        this.taskListService = taskListService;
    }

    /**
     * Affiche le formulaire de création de tâche pour un utilisateur spécifié.
     *
     * Cette méthode affiche le formulaire de création de tâche en préparant le modèle (Model) avec une
     * nouvelle instance de TaskForm et en ajoutant la liste des tâches appartenant à l'utilisateur spécifié.
     *
     * @param model Le modèle (Model) utilisé pour transmettre des données à la vue.
     * @param login Le nom d'utilisateur de l'utilisateur pour lequel la tâche est créée.
     * @return Le nom de la vue à afficher pour le formulaire de création de tâche (par exemple, "task/create").
     */
    @GetMapping("/create/{login}")
    public String create(Model model, @PathVariable String login){
        model.addAttribute("task", new TaskForm());
        model.addAttribute("taskList", taskListService.getAllAsOwner(login));
        return "/task/create";
    }

    /**
     * Traite la soumission du formulaire de création de tâche.
     *
     * Cette méthode gère la soumission du formulaire de création de tâche en vérifiant d'abord s'il y a
     * des erreurs de validation dans le formulaire. Si des erreurs sont détectées, la vue du formulaire est
     * réaffichée avec les messages d'erreur appropriés. Sinon, la tâche est créée à l'aide des données du
     * formulaire et l'utilisateur est redirigé vers une autre page, par exemple, la liste des tâches.
     *
     * @param form Le formulaire TaskForm soumis pour créer la tâche.
     * @param bindingResult Le résultat de la validation du formulaire.
     * @return Le nom de la vue à afficher après la soumission du formulaire (par exemple, "task/create" en cas
     *         d'erreurs de validation, ou une redirection vers une autre page en cas de succès).
     */
    @PostMapping("/create")
    public String processCreate(@ModelAttribute("task") @Valid TaskForm form, BindingResult bindingResult){

        if(bindingResult.hasErrors() ) {
            return "task/create";
        }
        
        taskService.create(form);
        return "redirect:/task/all/yann";
    }

    /**
     * Affiche le formulaire de mise à jour d'une tâche spécifique.
     *
     * Cette méthode affiche le formulaire de mise à jour d'une tâche en préparant le modèle (Model) avec
     * l'identifiant de la tâche à mettre à jour et les informations de la tâche actuelle à partir des données
     * récupérées par le service. Si la tâche n'est pas trouvée, une exception de type NotFoundException est levée.
     * 
     * La TaskEntity est mappée (convertit) en Task pour ne pas avoir d'entité dans le controller
     *
     * @param model Le modèle (Model) utilisé pour transmettre des données à la vue.
     * @param id L'id de la tâche à mettre à jour.
     * @return Le nom de la vue à afficher pour le formulaire de mise à jour de tâche (par exemple, "task/update").
     * @throws NotFoundException Si la tâche correspondant à l'identifiant spécifié n'est pas trouvée, une exception
     *                          NotFoundException est levée pour indiquer que la tâche n'a pas été trouvée.
     */
    @GetMapping("/update/{id:[0-9]+}")
    public String update(Model model, @PathVariable Long id){
        model.addAttribute("id", id);
        model.addAttribute("taskList", taskListService.getAllAsOwner("yann"));
        model.addAttribute("task", Task.toDTO(taskService.getOne(id).orElseThrow(()->new NotFoundException("ah que non"))));
        return "/task/update";
    }

    /**
     * Traite la soumission du formulaire de mise à jour d'une tâche.
     *
     * Cette méthode gère la soumission du formulaire de mise à jour de tâche en vérifiant d'abord s'il y a des
     * erreurs de validation dans le formulaire. Si des erreurs sont détectées, la vue du formulaire est réaffichée
     * avec les messages d'erreur appropriés. Sinon, la tâche est mise à jour à l'aide des données du formulaire et
     * l'utilisateur est redirigé vers une autre page, par exemple, la liste des tâches.
     *
     * @param form Le formulaire TaskForm soumis pour mettre à jour la tâche.
     * @param bindingResult Le résultat de la validation du formulaire.
     * @param id L'id de la tâche à mettre à jour.
     * @return Le nom de la vue à afficher après la soumission du formulaire (par exemple, "task/update" en cas d'erreurs
     *         de validation, ou une redirection vers une autre page en cas de succès).
     */
    @PostMapping("/update/{id:[0-9]+}")
    public String processUpdate(@ModelAttribute("task") @Valid TaskForm form, BindingResult bindingResult, @PathVariable Long id){

        if(bindingResult.hasErrors() ) {
            return "task/update";
        }

        taskService.update(id, form);
        return "redirect:/task/all/yann";
    }

    /**
     * Affiche la liste de toutes les tâches d'un utilisateur spécifié.
     *
     * Cette méthode récupère toutes les tâches de l'utilisateur identifié par le login et les prépare pour
     * l'affichage en les convertissant en DTO (Data Transfer Object). Elle transmet ces données au modèle
     * (Model) pour les afficher dans la vue "task/all".
     *
     * @param model Le modèle (Model) utilisé pour transmettre des données à la vue.
     * @param login Le nom d'utilisateur de l'utilisateur dont les tâches doivent être affichées.
     * @return Le nom de la vue à afficher pour la liste de toutes les tâches de l'utilisateur (par exemple, "task/all").
     */
    @GetMapping("/all/{login}")
    public String findAll(Model model, @PathVariable String login){
        model.addAttribute("set",taskService.getAll(login).stream().map(Task::toDTO).collect(Collectors.toSet()));
        return "/task/all";
    }

    /**
     * Marque une tâche spécifique comme complète et redirige vers les tâches complétées.
     *
     * Cette méthode marque une tâche spécifique comme complète en utilisant son identifiant unique et redirige
     * l'utilisateur vers une autre page, par exemple, la liste des tâches complétées.
     *
     * @param id L'identifiant unique de la tâche à marquer comme complète.
     * @return La redirection vers la liste des tâches complétées (par exemple, "redirect:/task/done/yann").
     */
    @GetMapping("/{id:[0-9]+}")
    public String finished(@PathVariable Long id){
        taskService.complete(id);
        return "redirect:/task/done/yann";
    }

    /**
     * Affiche la liste de tâches en attente pour un utilisateur spécifié.
     *
     * Cette méthode récupère toutes les tâches en attente de l'utilisateur identifié par le login, les convertit
     * en DTO (Data Transfer Object) et les prépare pour l'affichage. Les données sont ensuite transmises au modèle
     * (Model) pour les afficher dans la vue "task/pending".
     *
     * @param model Le modèle (Model) utilisé pour transmettre des données à la vue.
     * @param login Le nom d'utilisateur de l'utilisateur dont les tâches en attente doivent être affichées.
     * @return Le nom de la vue à afficher pour la liste des tâches en attente de l'utilisateur (par exemple, "task/pending").
     */
    @GetMapping("/pending/{login}")
    public String findPendingTasks(Model model, @PathVariable String login){
        model.addAttribute("set", taskService.getAllByStatus(login, TaskStatus.PENDING).stream().map(Task::toDTO).collect(Collectors.toSet()));
        return "/task/pending";
    }

    /**
     * Affiche la liste de tâches complétées pour un utilisateur spécifié.
     *
     * Cette méthode récupère toutes les tâches complétées de l'utilisateur identifié par le login, les convertit
     * en DTO (Data Transfer Object) et les prépare pour l'affichage. Les données sont ensuite transmises au modèle
     * (Model) pour les afficher dans la vue "task/done".
     *
     * @param model Le modèle (Model) utilisé pour transmettre des données à la vue.
     * @param login Le nom d'utilisateur de l'utilisateur dont les tâches complétées doivent être affichées.
     * @return Le nom de la vue à afficher pour la liste des tâches complétées de l'utilisateur (par exemple, "task/done").
     */
    @GetMapping("/done/{login}")
    public String findDoneTasks(Model model, @PathVariable String login){
        model.addAttribute("set", taskService.getAllByStatus(login, TaskStatus.DONE).stream().map(Task::toDTO).collect(Collectors.toSet()));
        return "/task/done";
    }

    /**
     * Supprime toutes les tâches complétées et redirige vers la liste de toutes les tâches.
     *
     * Cette méthode supprime de manière permanente toutes les tâches qui ont été marquées comme complètes
     * (statut "DONE") et redirige l'utilisateur vers la liste de toutes les tâches.
     *
     * @return La redirection vers la liste de toutes les tâches (par exemple, "redirect:/task/all/yann").
     */
    @GetMapping("/deleteAllFinished")
    public String deleteAllCompleted(){
        taskService.deleteAllWhereComplete();
        return "redirect:/task/all/yann";
    }

    /**
     * Supprime une tâche spécifique en utilisant son identifiant unique et redirige vers la liste de toutes les tâches.
     *
     * Cette méthode supprime de manière permanente une tâche spécifique en utilisant son identifiant unique et
     * redirige l'utilisateur vers la liste de toutes les tâches.
     *
     * @param id L'identifiant unique de la tâche à supprimer.
     * @return La redirection vers la liste de toutes les tâches (par exemple, "redirect:/task/all/yann").
     */
    @GetMapping("/delete/{id:[0-9]+}")
    public String delete(@PathVariable Long id){
        taskService.delete(id);
        return "redirect:/task/all/yann";
    }
    
}
