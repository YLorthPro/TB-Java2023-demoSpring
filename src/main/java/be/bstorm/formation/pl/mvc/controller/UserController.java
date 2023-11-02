package be.bstorm.formation.pl.mvc.controller;

import be.bstorm.formation.pl.models.forms.RegisterForm;
import be.bstorm.formation.bll.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Affiche le formulaire d'inscription d'un nouvel utilisateur.
     *
     * Cette méthode affiche le formulaire d'inscription d'un nouvel utilisateur en préparant le modèle (Model) avec une
     * nouvelle instance de RegisterForm. Elle permet aux utilisateurs de saisir leurs informations d'inscription.
     *
     * @param model Le modèle (Model) utilisé pour transmettre des données à la vue.
     * @return Le nom de la vue à afficher pour le formulaire d'inscription (par exemple, "auth/register").
     */
    @GetMapping("/register")
    public String create(Model model){
        model.addAttribute("form", new RegisterForm());
        return "/auth/register";
    }

    /**
     * Traite la soumission du formulaire d'inscription d'un nouvel utilisateur.
     *
     * Cette méthode gère la soumission du formulaire d'inscription d'un nouvel utilisateur en vérifiant d'abord s'il y a des
     * erreurs de validation dans le formulaire. Si des erreurs sont détectées, la vue du formulaire est réaffichée avec les
     * messages d'erreur appropriés. Sinon, un nouvel utilisateur est enregistré à l'aide des données du formulaire et
     * l'utilisateur est redirigé vers la page d'accueil de l'application.
     *
     * @param form Le formulaire RegisterForm soumis pour l'inscription d'un nouvel utilisateur.
     * @param bindingResult Le résultat de la validation du formulaire.
     * @return Le nom de la vue à afficher après la soumission du formulaire (par exemple, "auth/register" en cas d'erreurs
     *         de validation, ou une redirection vers la page d'accueil en cas de succès).
     */
    @PostMapping("/register")
    public String processCreate(@ModelAttribute @Valid RegisterForm form, BindingResult bindingResult){

        if(bindingResult.hasErrors() ) {
            return "auth/register";
        }

        userService.register(form);
        return "redirect:/";
    }
}
