package com.subscriptionservice.repository;

import com.subscriptionservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностью User.
 * Предоставляет методы для поиска и управления пользователями.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Проверяет существование пользователя с указанным email.
     *
     * @param email email для проверки
     * @return true, если пользователь существует, false в противном случае
     */
    boolean existsByEmail(String email);
} 