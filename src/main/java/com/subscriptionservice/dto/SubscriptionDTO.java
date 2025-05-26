package com.subscriptionservice.dto;

import com.subscriptionservice.validation.SubscriptionDateConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) для передачи данных подписки.
 * Используется для валидации и передачи данных между слоями приложения.
 */
@Data
@SubscriptionDateConstraint
public class SubscriptionDTO {
    /**
     * Уникальный идентификатор подписки
     */
    private Long id;

    /**
     * Название сервиса подписки
     */
    @NotBlank(message = "Название сервиса не может быть пустым")
    private String serviceName;

    /**
     * Дата начала подписки
     */
    @NotNull(message = "Дата начала подписки не может быть пустой")
    private LocalDateTime startDate;

    /**
     * Дата окончания подписки
     */
    @NotNull(message = "Дата окончания подписки не может быть пустой")
    private LocalDateTime endDate;

    /**
     * Идентификатор пользователя, которому принадлежит подписка
     */
    private Long userId;

    /**
     * Количество подписчиков на сервис.
     * Используется только для отображения статистики.
     */
    private Long subscriberCount;
} 