package com.ex.sothat.domain.api;

import com.ex.sothat.domain.dao.Project;
import com.ex.sothat.domain.app.ProjectService;
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

    private final List<TaskRequest> taskRequests = new ArrayList<>();

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
        model.addAttribute("tasks", taskRequests);
        model.addAttribute("newTask", new TaskRequest());
        return "boardPage";
    }
    @PostMapping("/add-task")
    public String addTask(@ModelAttribute TaskRequest taskRequest) {
        taskRequests.add(taskRequest);
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
        log.info("homePage 접근: principal={}", principal);
        if (principal != null) {
            model.addAttribute("name", principal.getAttribute("name"));
            model.addAttribute("email", principal.getAttribute("email"));

        } else {
            log.warn("인증이 된 사용자 정보 없음.");
        }
        return "homePage";
    }
}
