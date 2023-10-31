package be.bstorm.formation.pl.mvc.controller;

import be.bstorm.formation.pl.mvc.models.dto.Task;
import be.bstorm.formation.pl.mvc.models.forms.TaskForm;
import be.bstorm.formation.dal.models.entities.Status;
import be.bstorm.formation.bll.models.exception.NotFoundException;
import be.bstorm.formation.bll.service.TaskListService;
import be.bstorm.formation.bll.service.TaskService;
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
    private final TaskListService taskListService;
    public TaskController(TaskService taskService, TaskListService taskListService) {
        this.taskService = taskService;
        this.taskListService = taskListService;
    }

    @GetMapping("/create/{login}")
    public String create(Model model, @PathVariable String login){
        model.addAttribute("task", new TaskForm());
        model.addAttribute("taskList", taskListService.getAllAsOwner(login));
        return "/task/create";
    }

    @PostMapping("/create")
    public String processCreate(@ModelAttribute("task") @Valid TaskForm form, BindingResult bindingResult){

        if(bindingResult.hasErrors() ) {
            return "task/create";
        }
        
        taskService.create(form);
        return "redirect:/task/all/yann";
    }

    @GetMapping("/update/{id:[0-9]+}")
    public String update(Model model, @PathVariable Long id){
        model.addAttribute("id", id);
        model.addAttribute("task", Task.toDTO(taskService.getOne(id).orElseThrow(()->new NotFoundException("ah que non"))));
        return "/task/update";
    }

    @PostMapping("/update/{id:[0-9]+}")
    public String processUpdate(@ModelAttribute("task") @Valid TaskForm form, BindingResult bindingResult, @PathVariable Long id){

        if(bindingResult.hasErrors() ) {
            return "task/update";
        }

        taskService.update(id, form);
        return "redirect:/task/all/yann";
    }

    @GetMapping("/all/{login}")
    public String findAll(Model model, @PathVariable String login){
        model.addAttribute("set",taskService.getAll(login).stream().map(Task::toDTO).collect(Collectors.toSet()));
        return "/task/all";
    }

    @GetMapping("/{id:[0-9]+}")
    public String finished(@PathVariable Long id){
        taskService.complete(id);
        return "redirect:/task/done/yann";
    }

    @GetMapping("/pending/{login}")
    public String findPendingTasks(Model model, @PathVariable String login){
        model.addAttribute("set", taskService.getAllByStatus(login, Status.PENDING).stream().map(Task::toDTO).collect(Collectors.toSet()));
        return "/task/pending";
    }

    @GetMapping("/done/{login}")
    public String findDoneTasks(Model model, @PathVariable String login){
        model.addAttribute("set", taskService.getAllByStatus(login, Status.DONE).stream().map(Task::toDTO).collect(Collectors.toSet()));
        return "/task/done";
    }

    @GetMapping("/deleteAllFinished")
    public String deleteAllCompleted(){
        taskService.deleteAllWhereComplete();
        return "redirect:/task/all/yann";
    }

    @GetMapping("/delete/{id:[0-9]+}")
    public String delete(@PathVariable Long id){
        taskService.delete(id);
        return "redirect:/task/all/yann";
    }
    
}
