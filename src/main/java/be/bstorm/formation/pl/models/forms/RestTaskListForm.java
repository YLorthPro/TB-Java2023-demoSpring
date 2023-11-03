package be.bstorm.formation.pl.models.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;


@Data
public class RestTaskListForm {
    @NotBlank
    @Size(min=3)
    private String name;
    
    private List<String> viewersEntitiesLogin;

}
