package com.subscriptionservice.service;

import com.subscriptionservice.dto.SubscriptionDTO;
import com.subscriptionservice.dto.TopSubscriptionDTO;
import com.subscriptionservice.exception.ResourceNotFoundException;
import com.subscriptionservice.exception.ValidationException;
import com.subscriptionservice.mapper.SubscriptionMapper;
import com.subscriptionservice.mapper.UserMapper;
import com.subscriptionservice.model.Subscription;
import com.subscriptionservice.model.User;
import com.subscriptionservice.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления подписками пользователей.
 * Обеспечивает бизнес-логику для создания, получения и удаления подписок,
 * а также для получения статистики по популярным подпискам.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;
    private final SubscriptionMapper subscriptionMapper;

    /**
     * Добавляет новую подписку для пользователя.
     *
     * @param userId идентификатор пользователя
     * @param subscriptionDTO данные новой подписки
     * @return созданная подписка
     * @throws ValidationException если дата окончания подписки раньше даты начала
     * @throws ResourceNotFoundException если пользователь не найден
     */
    @Transactional
    public SubscriptionDTO addSubscription(Long userId, SubscriptionDTO subscriptionDTO) {
        log.info("Добавление подписки {} для пользователя с ID: {}", 
                subscriptionDTO.getServiceName(), userId);

        if (subscriptionRepository.existsByUserIdAndServiceName(userId, subscriptionDTO.getServiceName())) {
            log.warn("Попытка добавления существующей подписки {} для пользователя с ID: {}", 
                    subscriptionDTO.getServiceName(), userId);
            throw new ValidationException("У пользователя уже есть подписка на сервис " + subscriptionDTO.getServiceName());
        }

        if (subscriptionDTO.getEndDate().isBefore(subscriptionDTO.getStartDate())) {
            log.warn("Попытка добавления подписки с некорректными датами для пользователя с ID: {}", userId);
            throw new ValidationException("Дата окончания подписки должна быть позже даты начала");
        }

        User user = userService.getUserEntityById(userId);
        Subscription subscription = subscriptionMapper.toEntity(subscriptionDTO);
        subscription.setUser(user);
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        log.info("Подписка успешно добавлена с ID: {}", savedSubscription.getId());
        return subscriptionMapper.toDTO(savedSubscription);
    }

    /**
     * Получает список всех подписок пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список подписок пользователя
     */
    public List<SubscriptionDTO> getUserSubscriptions(Long userId) {
        log.info("Получение списка подписок для пользователя с ID: {}", userId);
        List<Subscription> subscriptions = subscriptionRepository.findByUserId(userId);
        log.info("Найдено {} подписок для пользователя", subscriptions.size());
        return subscriptions.stream()
                .map(subscriptionMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Удаляет подписку пользователя.
     *
     * @param userId идентификатор пользователя
     * @param subscriptionId идентификатор подписки
     * @throws ResourceNotFoundException если подписка не найдена
     * @throws ValidationException если подписка не принадлежит пользователю
     */
    @Transactional
    public void deleteSubscription(Long userId, Long subscriptionId) {
        log.info("Удаление подписки с ID: {} для пользователя с ID: {}", 
                subscriptionId, userId);
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> {
                    log.error("Подписка с ID {} не найдена", subscriptionId);
                    return new ResourceNotFoundException("Подписка не найдена");
                });
        
        if (!subscription.getUser().getId().equals(userId)) {
            log.error("Подписка с ID {} не принадлежит пользователю с ID {}", 
                    subscriptionId, userId);
            throw new ValidationException("Подписка не принадлежит пользователю");
        }
        
        subscriptionRepository.delete(subscription);
        log.info("Подписка успешно удалена");
    }

    /**
     * Получает список ТОП-3 популярных подписок.
     *
     * @return список DTO популярных подписок с количеством подписчиков
     */
    @Transactional(readOnly = true)
    public List<TopSubscriptionDTO> getTopSubscriptions() {
        log.info("Получение ТОП-3 популярных подписок");
        List<Object[]> results = subscriptionRepository.findTop3PopularSubscriptions();
        return results.stream()
                .map(result -> {
                    TopSubscriptionDTO dto = new TopSubscriptionDTO();
                    dto.setServiceName((String) result[0]);
                    dto.setSubscriberCount((Long) result[1]);
                    return dto;
                })
                .collect(Collectors.toList());
    }
} 