package be.bstorm.formation.bll.service;

import be.bstorm.formation.pl.mvc.models.forms.RegisterForm;

public interface UserService {
    void register(RegisterForm form);
}
