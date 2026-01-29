package com.ai.mvp.controller;

import com.ai.mvp.entity.Task;
import com.ai.mvp.entity.TaskStatus;
import com.ai.mvp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // REST API endpoints
    @GetMapping("/api")
    @ResponseBody
    public List<Task> getAllTasksApi() {
        return taskService.getAllTasks();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Task> getTaskByIdApi(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api")
    @ResponseBody
    public Task createTaskApi(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    // 登录页面
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Web界面端点
    @GetMapping
    public String getAllTasks(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        model.addAttribute("newTask", new Task());
        return "tasks";
    }

    @PostMapping
    public String createTask(@ModelAttribute Task task) {
        taskService.createTask(task);
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/complete")
    public String completeTask(@PathVariable Long id) {
        taskService.getTaskById(id).ifPresent(task -> {
            task.setStatus(TaskStatus.COMPLETED);
            taskService.updateTask(id, task);
        });
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }
}