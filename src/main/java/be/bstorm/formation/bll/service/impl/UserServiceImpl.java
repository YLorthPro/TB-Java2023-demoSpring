package be.bstorm.formation.bll.service.impl;

import be.bstorm.formation.bll.service.UserService;
import be.bstorm.formation.dal.models.entities.UserEntity;
import be.bstorm.formation.dal.models.entities.UserRole;
import be.bstorm.formation.pl.mvc.models.forms.RegisterForm;
import be.bstorm.formation.dal.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void register(RegisterForm form) {
        if (form == null)
            throw new RuntimeException("form can't be null");

        UserEntity entity = new UserEntity();
        entity.setFirstName(form.getFirstName());
        entity.setLastName(form.getLastName());
        entity.setLogin(form.getLogin());
        entity.setBirthdate(form.getBirthdate());
        entity.setPassword(form.getPassword());
        entity.setRole(UserRole.VISITOR);
        
        userRepository.save(entity);
    }
}
