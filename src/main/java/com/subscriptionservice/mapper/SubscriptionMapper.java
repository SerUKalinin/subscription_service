package com.subscriptionservice.mapper;

import com.subscriptionservice.dto.SubscriptionDTO;
import com.subscriptionservice.model.Subscription;
import org.springframework.stereotype.Component;

/**
 * Маппер для преобразования между сущностью Subscription и DTO.
 * Обеспечивает конвертацию данных между слоями приложения.
 */
@Component
public class SubscriptionMapper {

    /**
     * Преобразует сущность Subscription в SubscriptionDTO.
     *
     * @param subscription сущность подписки
     * @return DTO подписки
     */
    public SubscriptionDTO toDTO(Subscription subscription) {
        if (subscription == null) {
            return null;
        }

        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setId(subscription.getId());
        dto.setServiceName(subscription.getServiceName());
        dto.setStartDate(subscription.getStartDate());
        dto.setEndDate(subscription.getEndDate());
        if (subscription.getUser() != null) {
            dto.setUserId(subscription.getUser().getId());
        }
        return dto;
    }

    /**
     * Преобразует SubscriptionDTO в сущность Subscription.
     *
     * @param dto DTO подписки
     * @return сущность подписки
     */
    public Subscription toEntity(SubscriptionDTO dto) {
        if (dto == null) {
            return null;
        }

        Subscription subscription = new Subscription();
        subscription.setId(dto.getId());
        subscription.setServiceName(dto.getServiceName());
        subscription.setStartDate(dto.getStartDate());
        subscription.setEndDate(dto.getEndDate());
        return subscription;
    }
}