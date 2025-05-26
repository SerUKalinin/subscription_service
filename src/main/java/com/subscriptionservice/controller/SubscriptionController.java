package com.subscriptionservice.controller;

import com.subscriptionservice.dto.SubscriptionDTO;
import com.subscriptionservice.dto.TopSubscriptionDTO;
import com.subscriptionservice.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер для управления подписками пользователей.
 * Предоставляет REST API для добавления, получения и удаления подписок.
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "Subscription Management", description = "API для управления подписками")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    /**
     * Добавляет новую подписку для пользователя.
     *
     * @param userId идентификатор пользователя
     * @param subscriptionDTO данные новой подписки
     * @return созданная подписка
     */
    @Operation(summary = "Добавление новой подписки")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Подписка успешно создана"),
        @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PostMapping("/users/{userId}/subscriptions")
    public ResponseEntity<SubscriptionDTO> addSubscription(
            @Parameter(description = "ID пользователя") @PathVariable Long userId,
            @Valid @RequestBody SubscriptionDTO subscriptionDTO) {
        return ResponseEntity.status(201).body(subscriptionService.addSubscription(userId, subscriptionDTO));
    }

    /**
     * Получает список подписок пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список подписок пользователя
     */
    @Operation(summary = "Получение списка подписок пользователя")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список подписок успешно получен"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/users/{userId}/subscriptions")
    public ResponseEntity<List<SubscriptionDTO>> getUserSubscriptions(
            @Parameter(description = "ID пользователя") @PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionService.getUserSubscriptions(userId));
    }

    /**
     * Удаляет подписку пользователя.
     *
     * @param userId идентификатор пользователя
     * @param subscriptionId идентификатор подписки
     * @return пустой ответ с кодом 204 при успешном удалении
     */
    @Operation(summary = "Удаление подписки")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Подписка успешно удалена"),
        @ApiResponse(responseCode = "404", description = "Пользователь или подписка не найдены")
    })
    @DeleteMapping("/users/{userId}/subscriptions/{subscriptionId}")
    public ResponseEntity<Void> deleteSubscription(
            @Parameter(description = "ID пользователя") @PathVariable Long userId,
            @Parameter(description = "ID подписки") @PathVariable Long subscriptionId) {
        subscriptionService.deleteSubscription(userId, subscriptionId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получает список ТОП-3 популярных подписок.
     *
     * @return список популярных подписок с количеством подписчиков
     */
    @Operation(summary = "Получение ТОП-3 популярных подписок")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список популярных подписок успешно получен")
    })
    @GetMapping("/subscriptions/top")
    public ResponseEntity<List<TopSubscriptionDTO>> getTopSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getTopSubscriptions());
    }
} 