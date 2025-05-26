package com.subscriptionservice.mapper;

import com.subscriptionservice.dto.UserDTO;
import com.subscriptionservice.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Тесты маппера пользователей")
class UserMapperTest {

    private UserMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserMapper();
    }

    @Test
    @DisplayName("Преобразование сущности в DTO должно корректно маппить все поля")
    void toDTO_ShouldMapAllFields() {
        User user = new User();
        user.setId(1L);
        user.setUserName("testuser");
        user.setEmail("test@example.com");

        UserDTO dto = mapper.toDTO(user);

        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getUserName(), dto.getUserName());
        assertEquals(user.getEmail(), dto.getEmail());
    }

    @Test
    @DisplayName("Преобразование DTO в сущность должно корректно маппить все поля")
    void toEntity_ShouldMapAllFields() {
        UserDTO dto = new UserDTO();
        dto.setId(1L);
        dto.setUserName("testuser");
        dto.setEmail("test@example.com");

        User user = mapper.toEntity(dto);

        assertNotNull(user);
        assertEquals(dto.getUserName(), user.getUserName());
        assertEquals(dto.getEmail(), user.getEmail());
    }

    @Test
    @DisplayName("Обновление сущности из DTO должно корректно обновлять все поля")
    void updateEntity_ShouldUpdateAllFields() {
        User user = new User();
        user.setId(1L);
        user.setUserName("olduser");
        user.setEmail("old@example.com");

        UserDTO dto = new UserDTO();
        dto.setUserName("newuser");
        dto.setEmail("new@example.com");

        User updatedUser = mapper.updateEntity(user, dto);

        assertEquals(1L, updatedUser.getId()); // ID не должен измениться
        assertEquals(dto.getUserName(), updatedUser.getUserName());
        assertEquals(dto.getEmail(), updatedUser.getEmail());
    }

    @Test
    @DisplayName("Преобразование в DTO должно выбрасывать исключение при null-сущности")
    void toDTO_ShouldThrowException_WhenUserIsNull() {
        assertThrows(IllegalArgumentException.class, () -> mapper.toDTO(null));
    }

    @Test
    @DisplayName("Преобразование в сущность должно выбрасывать исключение при null-DTO")
    void toEntity_ShouldThrowException_WhenDTOIsNull() {
        assertThrows(IllegalArgumentException.class, () -> mapper.toEntity(null));
    }

    @Test
    @DisplayName("Обновление сущности должно выбрасывать исключение при null-сущности")
    void updateEntity_ShouldThrowException_WhenUserIsNull() {
        UserDTO dto = new UserDTO();
        dto.setUserName("testuser");
        dto.setEmail("test@example.com");

        assertThrows(IllegalArgumentException.class, () -> mapper.updateEntity(null, dto));
    }

    @Test
    @DisplayName("Обновление сущности должно выбрасывать исключение при null-DTO")
    void updateEntity_ShouldThrowException_WhenDTOIsNull() {
        User user = new User();
        user.setId(1L);
        user.setUserName("testuser");
        user.setEmail("test@example.com");

        assertThrows(IllegalArgumentException.class, () -> mapper.updateEntity(user, null));
    }
} 