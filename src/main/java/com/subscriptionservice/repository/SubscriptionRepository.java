package com.subscriptionservice.repository;

import com.subscriptionservice.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с подписками.
 * Предоставляет методы для доступа к данным подписок в базе данных.
 */
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    /**
     * Находит все подписки пользователя по его ID.
     *
     * @param userId идентификатор пользователя
     * @return список подписок пользователя
     */
    List<Subscription> findByUserId(Long userId);

    /**
     * Находит ТОП-3 самых популярных подписок.
     * Возвращает список массивов объектов, где каждый массив содержит:
     * [0] - название сервиса
     * [1] - количество подписок на этот сервис
     *
     * @return список популярных подписок с количеством пользователей
     */
    @Query("SELECT s.serviceName as serviceName, COUNT(s) as subscriberCount " +
           "FROM Subscription s " +
           "GROUP BY s.serviceName " +
           "ORDER BY subscriberCount DESC " +
           "LIMIT 3")
    List<Object[]> findTop3PopularSubscriptions();

    /**
     * Проверяет существование подписки на сервис у пользователя.
     *
     * @param userId идентификатор пользователя
     * @param serviceName название сервиса
     * @return true если подписка существует, false в противном случае
     */
    boolean existsByUserIdAndServiceName(Long userId, String serviceName);
} 