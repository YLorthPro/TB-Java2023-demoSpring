package be.bstorm.formation.pl.mvc.controller;

import be.bstorm.formation.bll.service.UserService;
import be.bstorm.formation.pl.mvc.models.dto.TaskList;
import be.bstorm.formation.bll.models.exception.NotFoundException;
import be.bstorm.formation.pl.mvc.models.dto.User;
import be.bstorm.formation.pl.mvc.models.forms.TaskListForm;
import be.bstorm.formation.bll.service.TaskListService;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/taskList")
public class TaskListController {
    private final TaskListService taskListService;
    private final UserService userService;

    public TaskListController(TaskListService taskListService, UserService userService) {
        this.taskListService = taskListService;
        this.userService = userService;
    }

    /**
     * Affiche la liste de toutes les listes de tâches d'un utilisateur spécifié.
     *
     * Cette méthode récupère toutes les listes de tâches de l'utilisateur identifié par le login et les prépare pour
     * l'affichage en les convertissant en DTO (Data Transfer Object). Les données sont ensuite transmises au modèle
     * (Model) pour les afficher dans la vue "taskList/all".
     *
     * @param model Le modèle (Model) utilisé pour transmettre des données à la vue.
     * @param login Le nom d'utilisateur de l'utilisateur dont les listes de tâches doivent être affichées.
     * @return Le nom de la vue à afficher pour la liste de toutes les listes de tâches de l'utilisateur (par exemple, "taskList/all").
     */
    @GetMapping("/all/{login}")
    public String getAll(Model model, @PathVariable String login){
        model.addAttribute("liste",taskListService.getAll(login));
        return "/taskList/all";
    }

    /**
     * Affiche une liste de tâches spécifique en fonction de son id.
     *
     * Cette méthode récupère une liste de tâches spécifique en utilisant son id et la prépare pour
     * l'affichage en la convertissant en DTO (Data Transfer Object). Les données sont ensuite transmises au modèle
     * (Model) pour les afficher dans la vue "taskList/one".
     *
     * @param model Le modèle (Model) utilisé pour transmettre des données à la vue.
     * @param id L'identifiant unique de la liste de tâches à afficher.
     * @return Le nom de la vue à afficher pour une liste de tâches spécifique (par exemple, "taskList/one").
     * @throws NotFoundException Si la liste de tâches correspondant à l'identifiant spécifié n'est pas trouvée, une exception
     *                          NotFoundException est levée pour indiquer que la liste de tâches n'a pas été trouvée.
     */
    @GetMapping("/{id:[0-9]+}")
    public String getOne(Model model, @PathVariable Long id){
        model.addAttribute("liste", TaskList.toDTO(taskListService.getOne(id).orElseThrow(()->new NotFoundException("Pas trouvé"))));
        return "/taskList/one";
    }

    /**
     * Affiche le formulaire de création d'une nouvelle liste de tâches pour un utilisateur spécifié.
     *
     * Cette méthode affiche le formulaire de création d'une nouvelle liste de tâches en préparant le modèle (Model) avec
     * une nouvelle instance de TaskListForm et en ajoutant le nom d'utilisateur du propriétaire de la liste de tâches.
     *
     * @param model Le modèle (Model) utilisé pour transmettre des données à la vue.
     * @param login Le nom d'utilisateur du propriétaire de la liste de tâches à créer.
     * @return Le nom de la vue à afficher pour le formulaire de création de liste de tâches (par exemple, "taskList/create").
     */
    @GetMapping("/create/{login}")
    public String create(Model model, @PathVariable String login){
        TaskListForm form = new TaskListForm();
        form.setOwner(login);
        model.addAttribute("users", userService.getAll().stream().map(User::toDTO).collect(Collectors.toSet()));
        model.addAttribute("form", form);
        return "/taskList/create";
    }

    /**
     * Traite la soumission du formulaire de création d'une nouvelle liste de tâches.
     *
     * Cette méthode gère la soumission du formulaire de création d'une nouvelle liste de tâches en vérifiant d'abord
     * s'il y a des erreurs de validation dans le formulaire. Si des erreurs sont détectées, la vue du formulaire est
     * réaffichée avec les messages d'erreur appropriés. Sinon, la liste de tâches est créée à l'aide des données du
     * formulaire et l'utilisateur est redirigé vers la liste de toutes les listes de tâches de l'utilisateur.
     *
     * @param form Le formulaire TaskListForm soumis pour créer la liste de tâches.
     * @param bindingResult Le résultat de la validation du formulaire.
     * @return Le nom de la vue à afficher après la soumission du formulaire (par exemple, "taskList/create" en cas
     *         d'erreurs de validation, ou une redirection vers la liste de toutes les listes de tâches en cas de succès).
     */
    @PostMapping("/create")
    public String processCreate(@ModelAttribute @Valid TaskListForm form, BindingResult bindingResult){

        if(bindingResult.hasErrors() ) {
            return "taskList/create";
        }

        taskListService.create(form);
        return "redirect:/taskList/all/"+form.getOwner();
    }

    /**
     * Affiche le formulaire de mise à jour d'une liste de tâches spécifique.
     *
     * Cette méthode récupère une liste de tâches spécifique en utilisant son id et la prépare pour
     * l'affichage en la convertissant en DTO (Data Transfer Object). Elle affiche ensuite le formulaire de mise à jour
     * de la liste de tâches en utilisant les données de la liste de tâches. Si la liste de tâches n'est pas trouvée, une
     * exception de type NotFoundException est levée.
     *
     * @param model Le modèle (Model) utilisé pour transmettre des données à la vue.
     * @param id L'identifiant unique de la liste de tâches à mettre à jour.
     * @return Le nom de la vue à afficher pour le formulaire de mise à jour de liste de tâches (par exemple, "taskList/update").
     * @throws NotFoundException Si la liste de tâches correspondant à l'identifiant spécifié n'est pas trouvée, une exception
     *                          NotFoundException est levéee.
     */
    @GetMapping("/update/{id:[0-9]+}")
    public String update(Model model, @PathVariable Long id){
        model.addAttribute("id",id);
        model.addAttribute("users", userService.getAll().stream().map(User::toDTO).collect(Collectors.toSet()));
        model.addAttribute("form", TaskList.toDTO(taskListService.getOne(id).orElseThrow(()->new NotFoundException("blabla"))));
        return "/taskList/update";
    }

    /**
     * Traite la soumission du formulaire de mise à jour d'une liste de tâches.
     *
     * Cette méthode gère la soumission du formulaire de mise à jour d'une liste de tâches en vérifiant d'abord s'il y a des
     * erreurs de validation dans le formulaire. Si des erreurs sont détectées, la vue du formulaire est réaffichée avec les
     * messages d'erreur appropriés. Sinon, la liste de tâches est mise à jour à l'aide des données du formulaire et
     * l'utilisateur est redirigé vers la liste de toutes les listes de tâches de l'utilisateur.
     *
     * @param form Le formulaire TaskListForm soumis pour mettre à jour la liste de tâches.
     * @param bindingResult Le résultat de la validation du formulaire.
     * @return Le nom de la vue à afficher après la soumission du formulaire (par exemple, "taskList/update" en cas
     *         d'erreurs de validation, ou une redirection vers la liste de toutes les listes de tâches de l'utilisateur en cas de succès).
     */
    @PostMapping("/update/{id:[0-9]+}")
    public String processUpdate(@ModelAttribute @Valid TaskListForm form, BindingResult bindingResult, @PathVariable Long id){

        if(bindingResult.hasErrors() ) {
            return "taskList/update";
        }

        taskListService.update(id, form);
        return "redirect:/taskList/all/"+form.getOwner();
    }

    /**
     * Supprime une liste de tâches spécifique en utilisant son id et redirige vers la liste de toutes les listes de tâches.
     *
     * Cette méthode supprime de manière permanente une liste de tâches spécifique en utilisant son id et
     * redirige l'utilisateur vers la liste de toutes les listes de tâches de l'utilisateur.
     *
     * @param id L'identifiant unique de la liste de tâches à supprimer.
     * @return La redirection vers la liste de toutes les listes de tâches de l'utilisateur (par exemple, "redirect:/taskList/all/yann").
     */
    @GetMapping("/delete/{id:[0-9]+}")
    public String delete(@PathVariable Long id){
        taskListService.delete(id);
        return "redirect:/taskList/all/yann";
    }
}
