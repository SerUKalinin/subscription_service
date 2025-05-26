package com.subscriptionservice.controller;

import com.subscriptionservice.dto.SubscriptionDTO;
import com.subscriptionservice.dto.TopSubscriptionDTO;
import com.subscriptionservice.service.SubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubscriptionController.class)
@DisplayName("Тесты контроллера подписок")
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private SubscriptionService subscriptionService;

    private SubscriptionDTO subscriptionDTO;

    @BeforeEach
    void setUp() {
        subscriptionDTO = Instancio.of(SubscriptionDTO.class)
                .set(Select.field("startDate"), LocalDateTime.now())
                .set(Select.field("endDate"), LocalDateTime.now().plusMonths(1))
                .create();
    }

    @Test
    @DisplayName("Успешное добавление подписки")
    void addSubscription_Success() throws Exception {
        Mockito.when(subscriptionService.addSubscription(anyLong(), any(SubscriptionDTO.class))).thenReturn(subscriptionDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscriptionDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(subscriptionDTO.getId()))
                .andExpect(jsonPath("$.serviceName").value(subscriptionDTO.getServiceName()))
                .andExpect(jsonPath("$.userId").value(subscriptionDTO.getUserId()));

        Mockito.verify(subscriptionService).addSubscription(Mockito.eq(1L), any(SubscriptionDTO.class));
    }

    @Test
    @DisplayName("Успешное получение подписок пользователя")
    void getUserSubscriptions_Success() throws Exception {
        List<SubscriptionDTO> subscriptions = Arrays.asList(subscriptionDTO);
        Mockito.when(subscriptionService.getUserSubscriptions(anyLong())).thenReturn(subscriptions);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(subscriptionDTO.getId()))
                .andExpect(jsonPath("$[0].serviceName").value(subscriptionDTO.getServiceName()))
                .andExpect(jsonPath("$[0].userId").value(subscriptionDTO.getUserId()));

        Mockito.verify(subscriptionService).getUserSubscriptions(1L);
    }

    @Test
    @DisplayName("Успешное удаление подписки")
    void deleteSubscription_Success() throws Exception {
        Mockito.doNothing().when(subscriptionService).deleteSubscription(anyLong(), anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1/subscriptions/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(subscriptionService).deleteSubscription(1L, 1L);
    }

    @Test
    @DisplayName("Успешное получение топовых подписок")
    void getTopSubscriptions_Success() throws Exception {
        TopSubscriptionDTO topSubscriptionDTO = new TopSubscriptionDTO();
        topSubscriptionDTO.setServiceName("Netflix");
        topSubscriptionDTO.setSubscriberCount(5L);
        List<TopSubscriptionDTO> results = Arrays.asList(topSubscriptionDTO);
        
        Mockito.when(subscriptionService.getTopSubscriptions()).thenReturn(results);

        mockMvc.perform(MockMvcRequestBuilders.get("/subscriptions/top"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].serviceName").value("Netflix"))
                .andExpect(jsonPath("$[0].subscriberCount").value(5));

        Mockito.verify(subscriptionService).getTopSubscriptions();
    }

    @Test
    @DisplayName("Добавление подписки с некорректными данными")
    void addSubscription_InvalidData() throws Exception {
        subscriptionDTO.setEndDate(subscriptionDTO.getStartDate().minusDays(1));

        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscriptionDTO)))
                .andExpect(status().isBadRequest());

        Mockito.verify(subscriptionService, Mockito.never()).addSubscription(anyLong(), any(SubscriptionDTO.class));
    }
}
