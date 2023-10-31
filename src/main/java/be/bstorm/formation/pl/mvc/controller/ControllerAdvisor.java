package be.bstorm.formation.pl.mvc.controller;

import be.bstorm.formation.bll.models.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * ControllerAdvisor: Gestion des erreurs et l'ajout de fonctionnalités globales aux contrôleurs.
 *
 * Cette classe agit en tant que ControllerAdvice pour l'application Spring Boot. Elle
 * permet de gérer les exceptions de manière centralisée et d'ajouter des fonctionnalités globales aux contrôleurs.
 * Les méthodes de cette classe sont annotées avec des annotations spécifiques pour indiquer comment elles doivent
 * réagir aux exceptions ou ajouter des fonctionnalités globales.
 */
@ControllerAdvice
public class ControllerAdvisor {

    /**
     * Gère les exceptions de type NotFoundException en affichant une page d'erreur 404.
     *
     * Cette méthode est appelée lorsqu'une exception de type NotFoundException est levée dans l'application.
     * Elle récupère l'exception, ajoute l'exception à un modèle (Model) pour l'affichage sur la page d'erreur,
     * puis renvoie une vue correspondant à une page d'erreur 404.
     *
     * @param ex L'exception de type NotFoundException levée dans l'application.
     * @param model Le modèle (Model) utilisé pour transmettre des données à la vue.
     * @return Le nom de la vue à afficher pour l'erreur 404 ("error/404").
     */
    @ExceptionHandler(NotFoundException.class)
    public String handleObjectNotFound(NotFoundException ex, Model model){
        model.addAttribute("exception", ex);
        return "error/404";
    }

}
