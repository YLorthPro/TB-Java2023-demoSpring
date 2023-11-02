package be.bstorm.formation.pl.rest;

import be.bstorm.formation.bll.service.TaskService;
import be.bstorm.formation.pl.mvc.models.dto.Task;
import be.bstorm.formation.pl.mvc.models.forms.TaskForm;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/task")
public class RestTaskController {
    private final TaskService taskService;
    
    public RestTaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    
    @GetMapping("/all")
    public ResponseEntity<Set<Task>> getAll(){
        return ResponseEntity.ok(taskService.getAll("yann").stream().map(Task::toDTO).collect(Collectors.toSet()));
    }
    
    @PostMapping("/create")
    public void create(@RequestBody @Valid TaskForm form){
        taskService.create(form);
    }
    
}
