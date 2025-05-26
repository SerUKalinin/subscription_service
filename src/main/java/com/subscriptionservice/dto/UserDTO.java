package com.subscriptionservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO для передачи данных о пользователе.
 * Содержит валидацию полей для обеспечения корректности данных.
 */
@Data
public class UserDTO {
    /**
     * Уникальный идентификатор пользователя.
     * Генерируется автоматически при создании.
     */
    private Long id;

    /**
     * Имя пользователя.
     * Не может быть пустым и должно содержать от 2 до 50 символов.
     */
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 2, max = 50, message = "Имя пользователя должно содержать от 2 до 50 символов")
    private String userName;

    /**
     * Email пользователя.
     * Должен быть уникальным и соответствовать формату email.
     */
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    @Size(max = 100, message = "Email не может быть длиннее 100 символов")
    private String email;
} 