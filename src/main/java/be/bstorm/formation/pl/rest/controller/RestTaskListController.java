package be.bstorm.formation.pl.rest.controller;

import be.bstorm.formation.bll.models.exception.NotFoundException;
import be.bstorm.formation.bll.service.TaskListService;
import be.bstorm.formation.pl.models.dto.TaskList;
import be.bstorm.formation.pl.models.forms.TaskListForm;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/all/{login}")
    public ResponseEntity<Set<TaskList>> getAll(@PathVariable String login){
        return ResponseEntity.ok(taskListService.getAll(login).stream().map(TaskList::toDTO).collect(Collectors.toSet()));
    }
    
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<TaskList> getOne(@PathVariable Long id){
        return ResponseEntity.ok(TaskList.toDTO(taskListService.getOne(id).orElseThrow(()->new NotFoundException("Pas trouvé"))));
    }
    
    @PostMapping("/create")
    public void create(@RequestBody @Valid TaskListForm form){
        taskListService.create(form);
    }
    
    @PutMapping("/{id:[0-9]+}")
    public void update(@RequestBody @Valid TaskListForm form, @PathVariable Long id){
        taskListService.update(id, form);
    }
    
    @DeleteMapping("/{id:[0-9]+}")
    public void delete(@PathVariable Long id){
        taskListService.delete(id);
    }
}
