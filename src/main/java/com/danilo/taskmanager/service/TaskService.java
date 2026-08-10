package com.danilo.taskmanager.service;

import com.danilo.taskmanager.model.Task;
import com.danilo.taskmanager.model.TaskStatus;
import com.danilo.taskmanager.model.User;
import com.danilo.taskmanager.repository.TaskRepository;
import com.danilo.taskmanager.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found: " + username));
    }

    public List<Task> findAll() {
        return taskRepository.findByUser(getCurrentUser());
    }

    public Task findById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        if (!task.getUser().getId().equals(getCurrentUser().getId())) {
            throw new RuntimeException("You don't have permission to access this task");
        }

        return task;
    }

    public Task create(Task task) {
        task.setCreatedAt(LocalDate.now());
        task.setUser(getCurrentUser());
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.PENDING);
        }
        return taskRepository.save(task);
    }

    public Task update(Long id, Task updatedTask) {
        Task existingTask = findById(id); // já valida dono, por causa do findById acima

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStatus(updatedTask.getStatus());
        existingTask.setPriority(updatedTask.getPriority());
        existingTask.setDueDate(updatedTask.getDueDate());

        return taskRepository.save(existingTask);
    }

    public void delete(Long id) {
        Task task = findById(id); // já valida dono
        taskRepository.delete(task);
    }

    public List<Task> findByStatus(TaskStatus status) {
        return taskRepository.findByUserAndStatus(getCurrentUser(), status);
    }
}
