package com.subscriptionservice.service;

import com.subscriptionservice.dto.UserDTO;
import com.subscriptionservice.exception.ResourceNotFoundException;
import com.subscriptionservice.exception.UserAlreadyExistsException;
import com.subscriptionservice.mapper.UserMapper;
import com.subscriptionservice.model.User;
import com.subscriptionservice.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты сервиса пользователей")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setUserName("testuser");
        userDTO.setEmail("test@example.com");

        user = new User();
        user.setId(1L);
        user.setUserName("testuser");
        user.setEmail("test@example.com");
    }

    @Test
    @DisplayName("Создание пользователя — успешно")
    void createUser_success() {
        Mockito.when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
        Mockito.when(userMapper.toEntity(userDTO)).thenReturn(user);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        Mockito.when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.createUser(userDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(userDTO.getEmail(), result.getEmail());
        Mockito.verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Создание пользователя — email уже существует")
    void createUser_emailExists() {
        Mockito.when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userDTO));
        Mockito.verify(userRepository, Mockito.never()).save(any());
    }

    @Test
    @DisplayName("Получение пользователя по ID — успешно")
    void getUserById_success() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(userDTO.getEmail(), result.getEmail());
    }

    @Test
    @DisplayName("Получение пользователя по ID — пользователь не найден")
    void getUserById_notFound() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    @DisplayName("Обновление пользователя — успешно")
    void updateUser_success() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userMapper.updateEntity(user, userDTO)).thenReturn(user);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.updateUser(1L, userDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(userDTO.getEmail(), result.getEmail());
        Mockito.verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Обновление пользователя — пользователь не найден")
    void updateUser_notFound() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(1L, userDTO));
        Mockito.verify(userRepository, Mockito.never()).save(any());
    }

    @Test
    @DisplayName("Обновление пользователя — email уже существует")
    void updateUser_emailExists() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("old@example.com");

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.updateUser(1L, userDTO));
        Mockito.verify(userRepository, Mockito.never()).save(any());
    }

    @Test
    @DisplayName("Удаление пользователя — успешно")
    void deleteUser_success() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> userService.deleteUser(1L));
        Mockito.verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Удаление пользователя — пользователь не найден")
    void deleteUser_notFound() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1L));
        Mockito.verify(userRepository, Mockito.never()).deleteById(any());
    }
}
