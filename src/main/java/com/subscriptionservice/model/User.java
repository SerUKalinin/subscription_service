package com.subscriptionservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность пользователя системы.
 * Содержит информацию о пользователе и его подписках.
 */
@Data
@Entity
@Table(name = "users")
public class User {
    /**
     * Уникальный идентификатор пользователя.
     * Генерируется автоматически при создании.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя.
     */
    @Column(name = "user_name", nullable = false)
    private String userName;

    /**
     * Email пользователя.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Список подписок пользователя.
     * При удалении пользователя все его подписки удаляются автоматически.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Subscription> subscriptions = new ArrayList<>();

    /**
     * Дата и время создания записи.
     * Устанавливается автоматически при создании.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Дата и время последнего обновления записи.
     * Обновляется автоматически при изменении.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Устанавливает время создания и обновления перед сохранением.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Обновляет время последнего изменения при обновлении.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 