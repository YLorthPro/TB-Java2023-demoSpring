package be.bstorm.formation.pl.rest.controller;

import be.bstorm.formation.bll.service.TaskService;
import be.bstorm.formation.dal.models.enums.TaskStatus;
import be.bstorm.formation.pl.models.dto.Task;
import be.bstorm.formation.pl.models.forms.TaskForm;
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
    
    //CRUD
    
    //Read
    @GetMapping("/all/{login}")
    public ResponseEntity<Set<Task>> getAll(@PathVariable String login){
        return ResponseEntity.ok(taskService.getAll(login).stream().map(Task::toDTO).collect(Collectors.toSet()));
    }
    
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<Task> getOne(@PathVariable Long id){
        return ResponseEntity.ok(Task.toDTO(taskService.getOne(id).get()));
    }
    
    //Create
    
    @PostMapping("/create")
    public void create(@RequestBody @Valid TaskForm form){
        taskService.create(form);
    }
    
    //Update
    
    @PutMapping("/{id:[0-9]+}")
    public void update(@RequestBody @Valid TaskForm form, @PathVariable Long id){
        taskService.update(id, form);
    }
    
    //Delete
    
    @DeleteMapping("/{id:[0-9]+}")
    public void delete(@PathVariable Long id){
        taskService.delete(id);
    }
    
    //Other

    @GetMapping("/pending/{login}")
    public ResponseEntity<Set<Task>> findPendingTasks(@PathVariable String login){
        return ResponseEntity.ok(taskService.getAllByStatus(login, TaskStatus.PENDING).stream().map(Task::toDTO).collect(Collectors.toSet()));
    }

    @GetMapping("/done/{login}")
    public ResponseEntity<Set<Task>> findDoneTasks(@PathVariable String login){
        return ResponseEntity.ok(taskService.getAllByStatus(login, TaskStatus.DONE).stream().map(Task::toDTO).collect(Collectors.toSet()));
    }

    @DeleteMapping("/deleteAllFinished")
    public void deleteAllCompleted(){
        taskService.deleteAllWhereComplete();
    }

    @PatchMapping("/{id:[0-9]+}")
    public void finished(@PathVariable Long id){
        taskService.complete(id);
    }
}