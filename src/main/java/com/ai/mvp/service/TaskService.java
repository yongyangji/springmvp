package com.ai.mvp.service;

import com.ai.mvp.entity.Task;
import com.ai.mvp.entity.TaskStatus;
import com.ai.mvp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }


    public Optional<Task> getTaskById(Long id) {
        //增加注释
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task taskDetails) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(taskDetails.getTitle());
                    task.setDescription(taskDetails.getDescription());
                    task.setStatus(taskDetails.getStatus());
                    task.setPriority(taskDetails.getPriority());
                    task.setUpdatedAt(java.time.LocalDateTime.now());
                    return taskRepository.save(task);
                })
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatusOrderByPriorityDesc(status);
    }

    public List<Task> getHighPriorityTasks(Integer minPriority) {
        return taskRepository.findByPriorityGreaterThanEqualOrderByCreatedAtDesc(minPriority);
    }
}