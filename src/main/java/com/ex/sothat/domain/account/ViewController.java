package com.ex.sothat.domain.account;

import com.ex.sothat.domain.task.Task;
import com.ex.sothat.domain.project.Project;
import com.ex.sothat.domain.project.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class ViewController {
    private final List<Task> tasks = new ArrayList<>();

    @Autowired
    private ProjectService projectService;

    @GetMapping("/")
    public String indexPage(Model model) {
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        return "index";
    }
    @GetMapping("/boardPage")
    public String boardPage(Model model) {
        model.addAttribute("tasks", tasks);
        model.addAttribute("newTask", new Task());
        return "boardPage";
    }

    @PostMapping("/add-task")
    public String addTask(@ModelAttribute Task task) {
        tasks.add(task);
        return "redirect:/";
    }
    @GetMapping("/loginPage")
    public String loginPage() {
        return "loginPage";
    }
    @GetMapping("/joinPage")
    public String joinPage(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null) {
            model.addAttribute("name", principal.getAttribute("name"));
            model.addAttribute("email", principal.getAttribute("email"));
        }
        return "joinPage";
    }
    @GetMapping("/homePage")
    public String home(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null) {
            model.addAttribute("name", principal.getAttribute("name"));
            model.addAttribute("email", principal.getAttribute("email"));

        }
        return "homePage";
    }
}
