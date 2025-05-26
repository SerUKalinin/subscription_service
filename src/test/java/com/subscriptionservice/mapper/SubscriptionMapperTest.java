package com.subscriptionservice.mapper;

import com.subscriptionservice.dto.SubscriptionDTO;
import com.subscriptionservice.model.Subscription;
import com.subscriptionservice.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Тесты маппера подписок")
class SubscriptionMapperTest {

    private SubscriptionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SubscriptionMapper();
    }

    @Test
    @DisplayName("Преобразование сущности в DTO должно корректно маппить все поля")
    void toDTO_ShouldMapAllFields() {
        User user = new User();
        user.setId(1L);

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusMonths(1);

        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setServiceName("Netflix");
        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);
        subscription.setUser(user);

        SubscriptionDTO dto = mapper.toDTO(subscription);

        assertNotNull(dto);
        assertEquals(subscription.getId(), dto.getId());
        assertEquals(subscription.getServiceName(), dto.getServiceName());
        assertEquals(subscription.getStartDate(), dto.getStartDate());
        assertEquals(subscription.getEndDate(), dto.getEndDate());
        assertEquals(subscription.getUser().getId(), dto.getUserId());
    }

    @Test
    @DisplayName("Преобразование DTO в сущность должно корректно маппить все поля")
    void toEntity_ShouldMapAllFields() {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusMonths(1);

        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setId(1L);
        dto.setServiceName("Netflix");
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);
        dto.setUserId(1L);

        Subscription subscription = mapper.toEntity(dto);

        assertNotNull(subscription);
        assertEquals(dto.getId(), subscription.getId());
        assertEquals(dto.getServiceName(), subscription.getServiceName());
        assertEquals(dto.getStartDate(), subscription.getStartDate());
        assertEquals(dto.getEndDate(), subscription.getEndDate());
    }

    @Test
    @DisplayName("Преобразование в DTO должно возвращать null при null-сущности")
    void toDTO_ShouldReturnNull_WhenSubscriptionIsNull() {
        SubscriptionDTO dto = mapper.toDTO(null);

        assertNull(dto);
    }

    @Test
    @DisplayName("Преобразование в сущность должно возвращать null при null-DTO")
    void toEntity_ShouldReturnNull_WhenDTOIsNull() {
        Subscription subscription = mapper.toEntity(null);

        assertNull(subscription);
    }

    @Test
    @DisplayName("Преобразование в DTO должно корректно обрабатывать отсутствующего пользователя")
    void toDTO_ShouldHandleNullUser() {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusMonths(1);

        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setServiceName("Netflix");
        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);
        subscription.setUser(null);

        SubscriptionDTO dto = mapper.toDTO(subscription);

        assertNotNull(dto);
        assertEquals(subscription.getId(), dto.getId());
        assertEquals(subscription.getServiceName(), dto.getServiceName());
        assertEquals(subscription.getStartDate(), dto.getStartDate());
        assertEquals(subscription.getEndDate(), dto.getEndDate());
        assertNull(dto.getUserId());
    }
} 