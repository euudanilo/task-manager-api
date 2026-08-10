package com.danilo.taskmanager.repository;

import com.danilo.taskmanager.model.Task;
import com.danilo.taskmanager.model.TaskStatus;
import com.danilo.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser(User user);

    List<Task> findByUserAndStatus(User user, TaskStatus status);

}
