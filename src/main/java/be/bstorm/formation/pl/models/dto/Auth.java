package be.bstorm.formation.pl.models.dto;

import be.bstorm.formation.dal.models.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class Auth {

    private String token;
    private String login;
    private Set<UserRole> roles;

}
