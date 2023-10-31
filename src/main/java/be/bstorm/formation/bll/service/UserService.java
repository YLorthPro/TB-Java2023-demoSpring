package be.bstorm.formation.bll.service;

import be.bstorm.formation.dal.models.entities.UserEntity;
import be.bstorm.formation.pl.mvc.models.forms.RegisterForm;

import java.util.Set;

public interface UserService {
    void register(RegisterForm form);
    Set<UserEntity> getAll();
}
