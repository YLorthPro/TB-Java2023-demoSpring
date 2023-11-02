package be.bstorm.formation.pl.models.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;


@Data
public class TaskListForm{
    @NotBlank
    @Size(min=3)
    private String name;
    
    @NotNull
    private String owner;
    
    private List<String> viewersEntitiesLogin;

}
