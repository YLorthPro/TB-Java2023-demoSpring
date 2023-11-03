package be.bstorm.formation.bll.service.impl;

import be.bstorm.formation.bll.service.UserService;
import be.bstorm.formation.dal.models.entities.UserEntity;
import be.bstorm.formation.dal.models.enums.UserRole;
import be.bstorm.formation.pl.config.security.JwtProvider;
import be.bstorm.formation.pl.models.dto.Auth;
import be.bstorm.formation.pl.models.forms.LoginForm;
import be.bstorm.formation.pl.models.forms.RegisterForm;
import be.bstorm.formation.dal.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public UserServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }


    @Override
    public Auth login(LoginForm form) {
        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(form.getLogin(),form.getPassword()) );

        UserEntity userEntity = userRepository.findByLogin(form.getLogin() ).get();

        String token = jwtProvider.generateToken(userEntity.getUsername(), List.copyOf(userEntity.getRoles()) );

        return Auth.builder()
                .token(token)
                .login(userEntity.getLogin())
                .roles(userEntity.getRoles())
                .build();
    }

    @Override
    public void register(RegisterForm form) {
        if (form == null)
            throw new IllegalArgumentException("form can't be null");

        UserEntity entity = new UserEntity();
        entity.setFirstName(form.getFirstName());
        entity.setLastName(form.getLastName());
        entity.setLogin(form.getLogin());
        entity.setBirthdate(form.getBirthdate());
        entity.setPassword(passwordEncoder.encode(form.getPassword()));
        Set<UserRole> set = new HashSet<>();
        set.add(UserRole.VISITOR);
        entity.setRoles(set);
        
        userRepository.save(entity);
    }

}
