package com.subscriptionservice.service;

import com.subscriptionservice.dto.SubscriptionDTO;
import com.subscriptionservice.dto.TopSubscriptionDTO;
import com.subscriptionservice.dto.UserDTO;
import com.subscriptionservice.exception.ResourceNotFoundException;
import com.subscriptionservice.exception.ValidationException;
import com.subscriptionservice.mapper.SubscriptionMapper;
import com.subscriptionservice.mapper.UserMapper;
import com.subscriptionservice.model.Subscription;
import com.subscriptionservice.model.User;
import com.subscriptionservice.repository.SubscriptionRepository;
import com.subscriptionservice.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@DisplayName("Тестирование SubscriptionService")
@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SubscriptionMapper subscriptionMapper;

    @InjectMocks
    private SubscriptionService subscriptionService;

    private User user;
    private UserDTO userDTO;
    private Subscription subscription;
    private SubscriptionDTO subscriptionDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUserName("Test User");
        user.setEmail("test@example.com");

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUserName("Test User");
        userDTO.setEmail("test@example.com");

        subscription = new Subscription();
        subscription.setId(1L);
        subscription.setServiceName("Netflix");
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(LocalDateTime.now().plusMonths(1));
        subscription.setUser(user);

        subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setId(1L);
        subscriptionDTO.setServiceName("Netflix");
        subscriptionDTO.setStartDate(LocalDateTime.now());
        subscriptionDTO.setEndDate(LocalDateTime.now().plusMonths(1));
        subscriptionDTO.setUserId(1L);
    }

    @Test
    @DisplayName("Добавление подписки успешно")
    void addSubscription_success() {
        Mockito.when(userService.getUserEntityById(1L)).thenReturn(user);
        Mockito.when(subscriptionMapper.toEntity(subscriptionDTO)).thenReturn(subscription);
        Mockito.when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);
        Mockito.when(subscriptionMapper.toDTO(subscription)).thenReturn(subscriptionDTO);

        SubscriptionDTO result = subscriptionService.addSubscription(1L, subscriptionDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(subscriptionDTO.getServiceName(), result.getServiceName());
        Mockito.verify(subscriptionRepository).save(any(Subscription.class));
    }

    @Test
    @DisplayName("Добавление подписки — неверные даты")
    void addSubscription_invalidDates() {
        subscriptionDTO.setEndDate(LocalDateTime.now().minusDays(1));

        Assertions.assertThrows(ValidationException.class,
            () -> subscriptionService.addSubscription(1L, subscriptionDTO));
        Mockito.verify(subscriptionRepository, Mockito.never()).save(any());
    }

    @Test
    @DisplayName("Получение подписок пользователя успешно")
    void getUserSubscriptions_success() {
        List<Subscription> subscriptions = Arrays.asList(subscription);
        Mockito.when(subscriptionRepository.findByUserId(1L)).thenReturn(subscriptions);
        Mockito.when(subscriptionMapper.toDTO(subscription)).thenReturn(subscriptionDTO);

        List<SubscriptionDTO> result = subscriptionService.getUserSubscriptions(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(subscriptionDTO.getServiceName(), result.get(0).getServiceName());
    }

    @Test
    @DisplayName("Удаление подписки успешно")
    void deleteSubscription_success() {
        Mockito.when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));
        Mockito.doNothing().when(subscriptionRepository).delete(subscription);

        subscriptionService.deleteSubscription(1L, 1L);

        Mockito.verify(subscriptionRepository).delete(subscription);
    }

    @Test
    @DisplayName("Удаление подписки — не найдена")
    void deleteSubscription_notFound() {
        Mockito.when(subscriptionRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
            () -> subscriptionService.deleteSubscription(1L, 1L));
        Mockito.verify(subscriptionRepository, Mockito.never()).delete(any());
    }

    @Test
    @DisplayName("Удаление подписки — не принадлежит пользователю")
    void deleteSubscription_notBelongsToUser() {
        User differentUser = new User();
        differentUser.setId(2L);
        differentUser.setUserName("Different User");
        differentUser.setEmail("different@example.com");
        subscription.setUser(differentUser);

        Mockito.when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        Assertions.assertThrows(ValidationException.class,
            () -> subscriptionService.deleteSubscription(1L, 1L));
        Mockito.verify(subscriptionRepository, Mockito.never()).delete(any());
    }

    @Test
    @DisplayName("Успешное получение топовых подписок")
    void getTopSubscriptions_Success() {
        Object[] subscriptionData = new Object[]{"Netflix", 5L};
        List<Object[]> topSubscriptions = Collections.singletonList(subscriptionData);
        
        Mockito.when(subscriptionRepository.findTop3PopularSubscriptions()).thenReturn(topSubscriptions);

        List<TopSubscriptionDTO> subscriptionDTOs = subscriptionService.getTopSubscriptions();

        Assertions.assertNotNull(subscriptionDTOs);
        Assertions.assertEquals(1, subscriptionDTOs.size());
        Assertions.assertEquals("Netflix", subscriptionDTOs.get(0).getServiceName());
        Assertions.assertEquals(5L, subscriptionDTOs.get(0).getSubscriberCount());
        Mockito.verify(subscriptionRepository).findTop3PopularSubscriptions();
    }
}
