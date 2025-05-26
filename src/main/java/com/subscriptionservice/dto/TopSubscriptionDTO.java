package com.subscriptionservice.dto;

import lombok.Data;

/**
 * DTO для отображения информации о популярных подписках.
 * Используется только для эндпоинта получения ТОП-3 подписок.
 */
@Data
public class TopSubscriptionDTO {
    /**
     * Название сервиса подписки
     */
    private String serviceName;

    /**
     * Количество подписчиков на сервис
     */
    private Long subscriberCount;
} 