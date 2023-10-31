package be.bstorm.formation.pl.mvc.models.dto;

import be.bstorm.formation.dal.models.entities.UserEntity;

public record User (
        String login
){
    public static User toDTO(UserEntity entity){
        return new User(entity.getLogin());
    }
}
