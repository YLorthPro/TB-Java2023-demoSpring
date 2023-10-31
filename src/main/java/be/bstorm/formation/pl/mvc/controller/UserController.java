package be.bstorm.formation.pl.mvc.controller;

import be.bstorm.formation.pl.mvc.models.forms.RegisterForm;
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

    @GetMapping("/register")
    public String create(Model model){
        model.addAttribute("form", new RegisterForm());
        return "/auth/register";
    }

    @PostMapping("/register")
    public String processCreate(@ModelAttribute @Valid RegisterForm form, BindingResult bindingResult){

        if(bindingResult.hasErrors() ) {
            return "auth/register";
        }

        userService.register(form);
        return "redirect:/";
    }
}
