package be.bstorm.formation.dal.models.entities;


import be.bstorm.formation.dal.models.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "Utilisateur")
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String lastName;
    private String firstName;
    private LocalDate birthdate;
    private String login;
    private String password;
    private UserRole role;
    
}
