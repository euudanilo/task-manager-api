package com.danilo.taskmanager.service;

import com.danilo.taskmanager.model.Task;
import com.danilo.taskmanager.model.TaskStatus;
import com.danilo.taskmanager.model.User;
import com.danilo.taskmanager.repository.TaskRepository;
import com.danilo.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import com.danilo.taskmanager.exception.TaskNotFoundException;
import com.danilo.taskmanager.exception.UnauthorizedAccessException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("danilo");
        testUser.setEmail("danilo@email.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication())
                .thenReturn(new UsernamePasswordAuthenticationToken("danilo", null));
        SecurityContextHolder.setContext(securityContext);

        lenient().when(userRepository.findByUsername("danilo")).thenReturn(Optional.of(testUser));
    }

    @Test
    void shouldCreateTaskWithPendingStatusByDefault() {
        when(userRepository.findByUsername("danilo")).thenReturn(Optional.of(testUser));

        Task task = new Task();
        task.setTitle("Estudar Spring Boot");

        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task result = taskService.create(task);

        assertThat(result.getStatus()).isEqualTo(TaskStatus.PENDING);
        assertThat(result.getUser()).isEqualTo(testUser);
        assertThat(result.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.findById(999L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void shouldThrowExceptionWhenAccessingAnotherUsersTask() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("maria");

        Task otherUsersTask = new Task();
        otherUsersTask.setId(5L);
        otherUsersTask.setUser(otherUser);

        when(taskRepository.findById(5L)).thenReturn(Optional.of(otherUsersTask));

        assertThatThrownBy(() -> taskService.findById(5L))
                .isInstanceOf(UnauthorizedAccessException.class);
    }

    @Test
    void shouldReturnOnlyTasksBelongingToCurrentUser() {
        Task task1 = new Task();
        task1.setTitle("Tarefa 1");
        task1.setUser(testUser);

        Task task2 = new Task();
        task2.setTitle("Tarefa 2");
        task2.setUser(testUser);

        when(taskRepository.findByUser(testUser)).thenReturn(java.util.List.of(task1, task2));

        var result = taskService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(task1, task2);
    }

}
