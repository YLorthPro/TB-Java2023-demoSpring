package be.bstorm.formation.bll.service;

import be.bstorm.formation.pl.models.dto.Auth;
import be.bstorm.formation.pl.models.forms.LoginForm;
import be.bstorm.formation.pl.models.forms.RegisterForm;

public interface UserService {
    Auth login(LoginForm form);
    void register(RegisterForm form);
}
