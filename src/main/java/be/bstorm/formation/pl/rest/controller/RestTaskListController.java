package be.bstorm.formation.pl.rest.controller;

import be.bstorm.formation.bll.models.exception.NotFoundException;
import be.bstorm.formation.bll.service.TaskListService;
import be.bstorm.formation.pl.models.dto.TaskList;
import be.bstorm.formation.pl.models.forms.RestTaskListForm;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/taskList")
public class RestTaskListController {

    private final TaskListService taskListService;

    public RestTaskListController(TaskListService taskListService) {
        this.taskListService = taskListService;
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Set<TaskList>> getAll(Authentication authentication){
        return ResponseEntity.ok(taskListService.getAll(authentication.getName()).stream().map(TaskList::toDTO).collect(Collectors.toSet()));
    }
    
    @GetMapping("/{id:[0-9]+}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaskList> getOne(@PathVariable Long id, Authentication authentication){
        Set<TaskList> taskListSet = taskListService.getAll(authentication.getName()).stream().map(TaskList::toDTO).collect(Collectors.toSet());
        if (taskListSet.stream().noneMatch(taskList -> taskList.getId()==id)) {
            throw new RuntimeException("Not authorized");
        }
        return ResponseEntity.ok(TaskList.toDTO(taskListService.getOne(id).orElseThrow(()->new NotFoundException("Pas trouvé"))));
    }
    
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public void create(@RequestBody @Valid RestTaskListForm form, Authentication authentication){
        taskListService.create(form, authentication.getName());
    }
    
    @PutMapping("/{id:[0-9]+}")
    @PreAuthorize("isAuthenticated()")
    public void update(@RequestBody @Valid RestTaskListForm form, @PathVariable Long id, Authentication authentication){
        if(!taskListService.getOne(id).get().getOwnerEntity().getLogin().equals(authentication.getName()))
            throw new RuntimeException("Not authorized");
        taskListService.update(id, form);
    }
    
    @DeleteMapping("/{id:[0-9]+}")
    @PreAuthorize("isAuthenticated()")
    public void delete(@PathVariable Long id, Authentication authentication){
        if(!taskListService.getOne(id).get().getOwnerEntity().getLogin().equals(authentication.getName()))
            throw new RuntimeException("Not authorized");
        taskListService.delete(id);
    }
}
