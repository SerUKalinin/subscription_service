package com.subscriptionservice.controller;

import com.subscriptionservice.dto.SubscriptionDTO;
import com.subscriptionservice.dto.TopSubscriptionDTO;
import com.subscriptionservice.dto.UserDTO;
import com.subscriptionservice.exception.ResourceNotFoundException;
import com.subscriptionservice.exception.ValidationException;
import com.subscriptionservice.service.SubscriptionService;
import com.subscriptionservice.service.UserService;
import com.subscriptionservice.mapper.SubscriptionMapper;
import com.subscriptionservice.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit тесты контроллера подписок")
class SubscriptionControllerTest {

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private UserService userService;

    @Mock
    private SubscriptionMapper subscriptionMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private SubscriptionController subscriptionController;

    private SubscriptionDTO subscriptionDTO;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUserName("testuser");
        userDTO.setEmail("test@example.com");

        subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setId(1L);
        subscriptionDTO.setServiceName("test-service");
        subscriptionDTO.setStartDate(LocalDateTime.now());
        subscriptionDTO.setEndDate(LocalDateTime.now().plusMonths(1));
        subscriptionDTO.setUserId(1L);
    }

    @Test
    @DisplayName("Успешное добавление подписки")
    void addSubscription_Success() {
        when(subscriptionService.addSubscription(anyLong(), any(SubscriptionDTO.class)))
                .thenReturn(subscriptionDTO);

        ResponseEntity<SubscriptionDTO> response = subscriptionController.addSubscription(1L, subscriptionDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(subscriptionDTO.getId(), response.getBody().getId());
        assertEquals(subscriptionDTO.getServiceName(), response.getBody().getServiceName());
        assertEquals(subscriptionDTO.getUserId(), response.getBody().getUserId());

        verify(subscriptionService).addSubscription(eq(1L), any(SubscriptionDTO.class));
    }

    @Test
    @DisplayName("Успешное получение подписок пользователя")
    void getUserSubscriptions_Success() {
        List<SubscriptionDTO> subscriptions = Arrays.asList(subscriptionDTO);
        when(subscriptionService.getUserSubscriptions(1L)).thenReturn(subscriptions);

        ResponseEntity<List<SubscriptionDTO>> response = subscriptionController.getUserSubscriptions(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(subscriptionDTO.getId(), response.getBody().get(0).getId());
        assertEquals(subscriptionDTO.getServiceName(), response.getBody().get(0).getServiceName());

        verify(subscriptionService).getUserSubscriptions(1L);
    }

    @Test
    @DisplayName("Успешное удаление подписки")
    void deleteSubscription_Success() {
        doNothing().when(subscriptionService).deleteSubscription(1L, 1L);

        ResponseEntity<Void> response = subscriptionController.deleteSubscription(1L, 1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(subscriptionService).deleteSubscription(1L, 1L);
    }

    @Test
    @DisplayName("Успешное получение топовых подписок")
    void getTopSubscriptions_Success() {
        TopSubscriptionDTO topSubscription = new TopSubscriptionDTO();
        topSubscription.setServiceName("Netflix");
        topSubscription.setSubscriberCount(5L);
        List<TopSubscriptionDTO> topSubscriptions = Arrays.asList(topSubscription);
        when(subscriptionService.getTopSubscriptions()).thenReturn(topSubscriptions);

        ResponseEntity<List<TopSubscriptionDTO>> response = subscriptionController.getTopSubscriptions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Netflix", response.getBody().get(0).getServiceName());
        assertEquals(5L, response.getBody().get(0).getSubscriberCount());

        verify(subscriptionService).getTopSubscriptions();
    }

    @Test
    @DisplayName("Добавление подписки — пользователь не найден")
    void addSubscription_UserNotFound() {
        when(subscriptionService.addSubscription(anyLong(), any(SubscriptionDTO.class)))
                .thenThrow(new ResourceNotFoundException("Пользователь не найден"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> subscriptionController.addSubscription(1L, subscriptionDTO));

        assertEquals("Пользователь не найден", exception.getMessage());
        verify(subscriptionService).addSubscription(eq(1L), any(SubscriptionDTO.class));
    }

    @Test
    @DisplayName("Добавление подписки — подписка уже существует")
    void addSubscription_AlreadyExists() {
        when(subscriptionService.addSubscription(anyLong(), any(SubscriptionDTO.class)))
                .thenThrow(new ValidationException("У пользователя уже есть подписка на сервис"));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> subscriptionController.addSubscription(1L, subscriptionDTO));

        assertEquals("У пользователя уже есть подписка на сервис", exception.getMessage());
        verify(subscriptionService).addSubscription(eq(1L), any(SubscriptionDTO.class));
    }

    @Test
    @DisplayName("Удаление подписки — подписка не найдена")
    void deleteSubscription_NotFound() {
        doThrow(new ResourceNotFoundException("Подписка не найдена"))
                .when(subscriptionService).deleteSubscription(1L, 1L);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> subscriptionController.deleteSubscription(1L, 1L));

        assertEquals("Подписка не найдена", exception.getMessage());
        verify(subscriptionService).deleteSubscription(1L, 1L);
    }

    @Test
    @DisplayName("Удаление подписки — подписка не принадлежит пользователю")
    void deleteSubscription_NotBelongsToUser() {
        doThrow(new ValidationException("Подписка не принадлежит пользователю"))
                .when(subscriptionService).deleteSubscription(1L, 1L);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> subscriptionController.deleteSubscription(1L, 1L));

        assertEquals("Подписка не принадлежит пользователю", exception.getMessage());
        verify(subscriptionService).deleteSubscription(1L, 1L);
    }
} 