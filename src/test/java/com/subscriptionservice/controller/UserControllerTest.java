package com.subscriptionservice.controller;

import com.subscriptionservice.dto.UserDTO;
import com.subscriptionservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тестирование UserController")
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUserName("Test User");
        userDTO.setEmail("test@example.com");
    }

    @Test
    @DisplayName("Создание пользователя успешно")
    void createUser_success() throws Exception {
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.userName").value(userDTO.getUserName()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));

        verify(userService).createUser(any(UserDTO.class));
    }

    @Test
    @DisplayName("Получение пользователя успешно")
    void getUser_success() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(userDTO);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.userName").value(userDTO.getUserName()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));

        verify(userService).getUserById(1L);
    }

    @Test
    @DisplayName("Обновление пользователя успешно")
    void updateUser_success() throws Exception {
        when(userService.updateUser(anyLong(), any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.userName").value(userDTO.getUserName()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));

        verify(userService).updateUser(eq(1L), any(UserDTO.class));
    }

    @Test
    @DisplayName("Удаление пользователя успешно")
    void deleteUser_success() throws Exception {
        doNothing().when(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }

    @Test
    @DisplayName("Создание пользователя с невалидными данными")
    void createUser_InvalidData() throws Exception {
        userDTO.setEmail("invalid-email");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserDTO.class));
    }
} 