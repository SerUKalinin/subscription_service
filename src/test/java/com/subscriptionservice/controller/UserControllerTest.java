package com.subscriptionservice.controller;

import com.subscriptionservice.dto.UserDTO;
import com.subscriptionservice.exception.ResourceNotFoundException;
import com.subscriptionservice.exception.ValidationException;
import com.subscriptionservice.service.UserService;
import com.subscriptionservice.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit тесты контроллера пользователей")
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        // Подготовка тестовых данных
        userDTO = new UserDTO();
        userDTO.setUserName("testuser");
        userDTO.setEmail("test" + System.currentTimeMillis() + "@example.com");
    }

    @Test
    @DisplayName("Успешное создание пользователя")
    void createUser_Success() {
        UserDTO createdUser = new UserDTO();
        createdUser.setId(4L);
        createdUser.setUserName(userDTO.getUserName());
        createdUser.setEmail(userDTO.getEmail());
        
        when(userService.createUser(any(UserDTO.class))).thenReturn(createdUser);

        ResponseEntity<UserDTO> response = userController.createUser(userDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createdUser.getId(), response.getBody().getId());
        assertEquals(createdUser.getUserName(), response.getBody().getUserName());
        assertEquals(createdUser.getEmail(), response.getBody().getEmail());

        verify(userService).createUser(any(UserDTO.class));
    }

    @Test
    @DisplayName("Успешное получение пользователя")
    void getUser_Success() {
        when(userService.getUserById(anyLong())).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.getUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userDTO.getId(), response.getBody().getId());
        assertEquals(userDTO.getUserName(), response.getBody().getUserName());
        assertEquals(userDTO.getEmail(), response.getBody().getEmail());

        verify(userService).getUserById(1L);
    }

    @Test
    @DisplayName("Успешное обновление пользователя")
    void updateUser_Success() {
        when(userService.updateUser(anyLong(), any(UserDTO.class))).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.updateUser(1L, userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userDTO.getId(), response.getBody().getId());
        assertEquals(userDTO.getUserName(), response.getBody().getUserName());
        assertEquals(userDTO.getEmail(), response.getBody().getEmail());

        verify(userService).updateUser(eq(1L), any(UserDTO.class));
    }

    @Test
    @DisplayName("Успешное удаление пользователя")
    void deleteUser_Success() {
        doNothing().when(userService).deleteUser(anyLong());

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).deleteUser(1L);
    }

    @Test
    @DisplayName("Создание пользователя с некорректными данными")
    void createUser_InvalidData() {
        userDTO.setUserName("a"); // слишком короткое имя
        when(userService.createUser(any(UserDTO.class)))
                .thenThrow(new ValidationException("Некорректные данные пользователя"));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.createUser(userDTO));

        assertEquals("Некорректные данные пользователя", exception.getMessage());
        verify(userService).createUser(any(UserDTO.class));
    }

    @Test
    @DisplayName("Создание пользователя — email уже существует")
    void createUser_EmailExists() {
        when(userService.createUser(any(UserDTO.class)))
                .thenThrow(new ValidationException("Email уже используется"));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.createUser(userDTO));

        assertEquals("Email уже используется", exception.getMessage());
        verify(userService).createUser(any(UserDTO.class));
    }

    @Test
    @DisplayName("Получение пользователя — пользователь не найден")
    void getUser_NotFound() {
        when(userService.getUserById(anyLong()))
                .thenThrow(new ResourceNotFoundException("Пользователь не найден"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userController.getUser(1L));

        assertEquals("Пользователь не найден", exception.getMessage());
        verify(userService).getUserById(1L);
    }

    @Test
    @DisplayName("Обновление пользователя — пользователь не найден")
    void updateUser_NotFound() {
        when(userService.updateUser(anyLong(), any(UserDTO.class)))
                .thenThrow(new ResourceNotFoundException("Пользователь не найден"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userController.updateUser(1L, userDTO));

        assertEquals("Пользователь не найден", exception.getMessage());
        verify(userService).updateUser(eq(1L), any(UserDTO.class));
    }

    @Test
    @DisplayName("Удаление пользователя — пользователь не найден")
    void deleteUser_NotFound() {
        doThrow(new ResourceNotFoundException("Пользователь не найден"))
                .when(userService).deleteUser(anyLong());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userController.deleteUser(1L));

        assertEquals("Пользователь не найден", exception.getMessage());
        verify(userService).deleteUser(1L);
    }
} 