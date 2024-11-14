package com.example.inventory.controller;

import com.example.inventory.dto.OrderDto;
import com.example.inventory.exceptions.OrderNotFoundException;
import com.example.inventory.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private OrderDto mockOrder;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);



        // Prepare a mock OrderDto
        mockOrder = OrderDto.builder()
                .id(1L)
                .orderDate(LocalDateTime.now())
                .totalAmount(BigDecimal.valueOf(150.00))
                .productIds(List.of(1L, 2L, 3L))
                .userId(1L)
                .build();
    }

    @Test
    void createOrder_ShouldReturnCreatedOrder() throws Exception {
        when(orderService.createOrder(any(OrderDto.class))).thenReturn(mockOrder);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockOrder)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalAmount").value(150.00));

        verify(orderService, times(1)).createOrder(any(OrderDto.class));
    }

    @Test
    void createOrder_ShouldReturnValidationErrors() throws Exception {
        OrderDto invalidOrder = new OrderDto();
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidOrder)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.productIds").exists());

        verify(orderService, never()).createOrder(any(OrderDto.class));
    }

    @Test
    void getOrderById_ShouldReturnOrder() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(mockOrder);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalAmount").value(150.00));

        verify(orderService, times(1)).getOrderById(1L);
    }

    @Test
    void getOrderById_ShouldReturnNotFound() throws Exception {
        when(orderService.getOrderById(999L)).thenThrow(new OrderNotFoundException("Order not found"));

        mockMvc.perform(get("/api/orders/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Order not found"))
                .andExpect(jsonPath("$.message").value("Order not found"));

        verify(orderService, times(1)).getOrderById(999L);
    }

    @Test
    void getAllOrders_ShouldReturnListOfOrders() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of(mockOrder));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].totalAmount").value(150.00));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void updateOrder_ShouldReturnUpdatedOrder() throws Exception {
        when(orderService.updateOrder(eq(1L), any(OrderDto.class))).thenReturn(mockOrder);

        mockMvc.perform(put("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalAmount").value(150.00));

        verify(orderService, times(1)).updateOrder(eq(1L), any(OrderDto.class));
    }
    @Test
    void deleteOrder_ShouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Order deleted successfully"));

        verify(orderService, times(1)).deleteOrder(1L);
    }

    @Test
    void deleteOrder_ShouldReturnNotFound() throws Exception {
        doThrow(new OrderNotFoundException("Order not found")).when(orderService).deleteOrder(999L);

        mockMvc.perform(delete("/api/orders/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Order not found"))
                .andExpect(jsonPath("$.message").value("Order not found"));

        verify(orderService, times(1)).deleteOrder(999L);
    }
}
