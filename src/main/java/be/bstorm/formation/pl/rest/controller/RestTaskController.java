package be.bstorm.formation.pl.rest.controller;

import be.bstorm.formation.bll.service.TaskService;
import be.bstorm.formation.dal.models.enums.TaskStatus;
import be.bstorm.formation.pl.models.dto.Task;
import be.bstorm.formation.pl.models.forms.TaskForm;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public ResponseEntity<Set<Task>> getAll(Authentication authentication){
        return ResponseEntity.ok(taskService.getAll(authentication.getName()).stream().map(Task::toDTO).collect(Collectors.toSet()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<Task> getOne(@PathVariable Long id, Authentication authentication){
        if(!(taskService.getOne(id).get().getTaskListEntity().getOwnerEntity().getLogin().equals(authentication.getName()) || taskService.getOne(id).get().getTaskListEntity().getViewersEntities().stream().anyMatch(e -> e.getLogin().equals(authentication.getName()))))
            throw new RuntimeException("Not authorized");
        return ResponseEntity.ok(Task.toDTO(taskService.getOne(id).get()));
    }
    
    //Create
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public void create(@RequestBody @Valid TaskForm form){
        taskService.create(form);
    }
    
    //Update
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id:[0-9]+}")
    public void update(@RequestBody @Valid TaskForm form, @PathVariable Long id, Authentication authentication){
        if(!(taskService.getOne(id).get().getTaskListEntity().getOwnerEntity().getLogin().equals(authentication.getName())))
            throw new RuntimeException("Not authorized");
        taskService.update(id, form);
    }
    
    //Delete
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id:[0-9]+}")
    public void delete(@PathVariable Long id, Authentication authentication){
        if(!(taskService.getOne(id).get().getTaskListEntity().getOwnerEntity().getLogin().equals(authentication.getName())))
            throw new RuntimeException("Not authorized");
        taskService.delete(id);
    }
    
    //Other
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/pending")
    public ResponseEntity<Set<Task>> findPendingTasks(Authentication authentication){
        return ResponseEntity.ok(taskService.getAllByStatus(authentication.getName(), TaskStatus.PENDING).stream().map(Task::toDTO).collect(Collectors.toSet()));
    }

    @GetMapping("/done")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Set<Task>> findDoneTasks(Authentication authentication){
        return ResponseEntity.ok(taskService.getAllByStatus(authentication.getName(), TaskStatus.DONE).stream().map(Task::toDTO).collect(Collectors.toSet()));
    }

    @DeleteMapping("/deleteAllFinished")
    @PreAuthorize("hasRole('UserRole.ADMIN')")
    public void deleteAllCompleted(){
        taskService.deleteAllWhereComplete();
    }

    @PatchMapping("/{id:[0-9]+}")
    @PreAuthorize("isAuthenticated()")
    public void finished(@PathVariable Long id){
        taskService.complete(id);
    }
}
