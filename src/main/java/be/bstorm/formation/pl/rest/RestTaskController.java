package be.bstorm.formation.pl.rest;

import be.bstorm.formation.bll.service.TaskService;
import be.bstorm.formation.pl.mvc.models.dto.Task;
import jakarta.persistence.SecondaryTable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
