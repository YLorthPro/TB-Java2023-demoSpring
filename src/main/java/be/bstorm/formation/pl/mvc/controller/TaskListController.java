package be.bstorm.formation.pl.mvc.controller;

import be.bstorm.formation.pl.mvc.models.dto.TaskList;
import be.bstorm.formation.bll.models.exception.NotFoundException;
import be.bstorm.formation.pl.mvc.models.forms.TaskListForm;
import be.bstorm.formation.bll.service.TaskListService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/taskList")
public class TaskListController {
    private final TaskListService taskListService;

    public TaskListController(TaskListService taskListService) {
        this.taskListService = taskListService;
    }
    
    @GetMapping("/all/{login}")
    public String getAll(Model model, @PathVariable String login){
        model.addAttribute("liste",taskListService.getAll(login));
        return "/taskList/all";
    }
    
    @GetMapping("/{id:[0-9]+}")
    public String getOne(Model model, @PathVariable Long id){
        model.addAttribute("liste", TaskList.toDTO(taskListService.getOne(id).orElseThrow(()->new NotFoundException("Pas trouvé"))));
        return "/taskList/one";
    }
    
    @GetMapping("/create/{login}")
    public String create(Model model, @PathVariable String login){
        TaskListForm form = new TaskListForm();
        form.setOwner(login);
        model.addAttribute("form", form);
        return "/taskList/create";
    }
    
    @PostMapping("/create")
    public String processCreate(@ModelAttribute @Valid TaskListForm form, BindingResult bindingResult){

        if(bindingResult.hasErrors() ) {
            return "taskList/create";
        }

        taskListService.create(form);
        return "redirect:/taskList/all/"+form.getOwner();
    }

    @GetMapping("/update/{id:[0-9]+}")
    public String update(Model model, @PathVariable Long id){
        model.addAttribute("id",id);
        model.addAttribute("form", TaskList.toDTO(taskListService.getOne(id).orElseThrow(()->new NotFoundException("blabla"))));
        return "/taskList/update";
    }

    @PostMapping("/update/{id:[0-9]+}")
    public String processUpdate(@ModelAttribute @Valid TaskListForm form, BindingResult bindingResult){

        if(bindingResult.hasErrors() ) {
            return "taskList/update";
        }

        taskListService.create(form);
        return "redirect:/taskList/all/"+form.getOwner();
    }

    @GetMapping("/delete/{id:[0-9]+}")
    public String delete(@PathVariable Long id){
        taskListService.delete(id);
        return "redirect:/taskList/all/yann";
    }
}
