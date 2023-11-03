package be.bstorm.formation.pl.models.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;


@Data
public class MVCTaskListForm extends RestTaskListForm{
    @NotNull
    private String owner;
}
