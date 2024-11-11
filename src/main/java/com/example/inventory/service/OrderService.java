package com.example.inventory.service;

import com.example.inventory.dto.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    OrderDto updateOrder(Long id, OrderDto orderDto);
    OrderDto getOrderById(Long id);

    List<OrderDto> getAllOrders();

    void deleteOrder(Long id);
}
