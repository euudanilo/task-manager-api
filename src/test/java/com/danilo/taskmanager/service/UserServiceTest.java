package com.danilo.taskmanager.service;

import com.danilo.taskmanager.dto.RegisterRequest;
import com.danilo.taskmanager.dto.UserResponse;
import com.danilo.taskmanager.exception.EmailAlreadyRegisteredException;
import com.danilo.taskmanager.exception.UsernameAlreadyTakenException;
import com.danilo.taskmanager.model.User;
import com.danilo.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("danilo");
        registerRequest.setEmail("danilo@email.com");
        registerRequest.setPassword("123456");
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        when(userRepository.existsByUsername("danilo")).thenReturn(false);
        when(userRepository.existsByEmail("danilo@email.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("hashedPassword123");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("danilo");
        savedUser.setEmail("danilo@email.com");
        savedUser.setPassword("hashedPassword123");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.register(registerRequest);

        assertThat(response.getUsername()).isEqualTo("danilo");
        assertThat(response.getEmail()).isEqualTo("danilo@email.com");
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    void shouldEncryptPasswordBeforeSaving() {
        when(userRepository.existsByUsername("danilo")).thenReturn(false);
        when(userRepository.existsByEmail("danilo@email.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("hashedPassword123");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.register(registerRequest);

        verify(passwordEncoder).encode("123456");
        verify(userRepository).save(argThat(user -> user.getPassword().equals("hashedPassword123")));
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyTaken() {
        when(userRepository.existsByUsername("danilo")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(registerRequest))
                .isInstanceOf(UsernameAlreadyTakenException.class)
                .hasMessageContaining("danilo");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyRegistered() {
        when(userRepository.existsByUsername("danilo")).thenReturn(false);
        when(userRepository.existsByEmail("danilo@email.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(registerRequest))
                .isInstanceOf(EmailAlreadyRegisteredException.class)
                .hasMessageContaining("danilo@email.com");

        verify(userRepository, never()).save(any(User.class));
    }

}