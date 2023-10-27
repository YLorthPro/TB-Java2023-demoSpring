package be.bstorm.formation.controller;

import be.bstorm.formation.models.dto.Task;
import be.bstorm.formation.models.entities.Status;
import be.bstorm.formation.models.forms.TaskForm;
import be.bstorm.formation.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;


@Controller
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("task", new TaskForm());
        return "/task/create";
    }

    @PostMapping("/create")
    public String processCreate(@ModelAttribute("task") @Valid TaskForm form, BindingResult bindingResult){

        if(bindingResult.hasErrors() ) {
            return "task/create";
        }
        
        taskService.create(form);
        return "redirect:/task/all";
    }

    @GetMapping("/all")
    public String findAll(Model model){
        model.addAttribute("set",taskService.getAll().stream().map(Task::toDTO).collect(Collectors.toSet()));
        return "/task/all";
    }

    @PutMapping("/{id}")
    public String finished(@PathVariable Long id){
        taskService.complete(id);
        return "redirect:/task";
    }

    @GetMapping("/pending")
    public String findPendingTasks(Model model){
        model.addAttribute("tasks", taskService.getAllByStatus(Status.PENDING).stream().map(Task::toDTO).collect(Collectors.toSet()));
        return "/task/pending";
    }

    @GetMapping("/done")
    public String findDoneTasks(Model model){
        model.addAttribute("tasks", taskService.getAllByStatus(Status.DONE).stream().map(Task::toDTO).collect(Collectors.toSet()));
        return "/task/pending";
    }

    @GetMapping("/deleteAllFinished")
    public String deleteAllCompleted(){
        taskService.deleteAllWhereComplete();
        return "redirect:/task";
    }
    
}
