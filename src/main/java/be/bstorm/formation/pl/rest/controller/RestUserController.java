package be.bstorm.formation.pl.rest.controller;

import be.bstorm.formation.bll.service.UserService;
import be.bstorm.formation.pl.models.dto.Auth;
import be.bstorm.formation.pl.models.forms.LoginForm;
import be.bstorm.formation.pl.models.forms.RegisterForm;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RestUserController {
    private final UserService userService;

    public RestUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Auth> login(@RequestBody @Valid LoginForm form){
        return ResponseEntity.ok(userService.login(form));
    }
    @PostMapping("/register")
    public void create(@RequestBody @Valid RegisterForm form){
        userService.register(form);
    }
}
