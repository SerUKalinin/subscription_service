package com.subscriptionservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Сущность подписки.
 * Представляет подписку пользователя на определенный сервис с указанием периода действия.
 */
@Data
@Entity
@Table(name = "subscriptions")
public class Subscription {
    /**
     * Уникальный идентификатор подписки
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название сервиса подписки
     */
    @Column(nullable = false)
    private String serviceName;

    /**
     * Дата начала подписки
     */
    @Column(nullable = false)
    private LocalDateTime startDate;

    /**
     * Дата окончания подписки
     */
    @Column(nullable = false)
    private LocalDateTime endDate;

    /**
     * Пользователь, которому принадлежит подписка
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
} 