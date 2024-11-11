package com.example.inventory.service.impl;

import com.example.inventory.exceptions.ProductNotFoundException;
import com.example.inventory.repo.OrderRepo;
import com.example.inventory.repo.UserRepo;
import com.example.inventory.repo.ProductRepo;
import com.example.inventory.dto.OrderDto;
import com.example.inventory.exceptions.OrderNotFoundException;
import com.example.inventory.model.Order;
import com.example.inventory.model.Product;
import com.example.inventory.model.User;
import com.example.inventory.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = new Order();
        BeanUtils.copyProperties(orderDto, order);

        // Set the order date to the current date and time if it's not provided
        order.setOrderDate(LocalDateTime.now());

        // Fetch products and calculate the total amount
        List<Product> products = orderDto.getProductIds().stream()
                .map(id -> productRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Product not found with id: " + id)))
                .collect(Collectors.toList());
        order.setProducts(products);

        // Calculate the total amount based on product prices
        BigDecimal totalAmount = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);

        // Fetch the user associated with the order
        User user = userRepo.findById(orderDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + orderDto.getUserId()));
        order.setUser(user);

        // Save the order to the database
        Order savedOrder = orderRepo.save(order);

        return mapToDto(savedOrder);
    }


    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        return mapToDto(order);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        orderRepo.delete(order);
    }

    @Override
    public OrderDto updateOrder(Long id, OrderDto orderDto) {
        // Check if the order exists in the database
        Order existingOrder = orderRepo.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        // Copy properties from DTO to the existing order entity, excluding id and orderDate
        BeanUtils.copyProperties(orderDto, existingOrder, "id", "orderDate", "totalAmount");

        // Set the order date to the current date and time when updating the order
        existingOrder.setOrderDate(LocalDateTime.now());

        // Fetch the list of products from productIds in the DTO and calculate the total amount
        List<Product> products = orderDto.getProductIds().stream()
                .map(productId -> productRepo.findById(productId)
                        .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId)))
                .collect(Collectors.toList());
        existingOrder.setProducts(products);

        // Recalculate the total amount based on the updated list of products
        BigDecimal totalAmount = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        existingOrder.setTotalAmount(totalAmount);

        // Handle the user from userId in the DTO
        User user = userRepo.findById(orderDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + orderDto.getUserId()));
        existingOrder.setUser(user);

        // Save the updated order and return the updated DTO
        Order updatedOrder = orderRepo.save(existingOrder);
        return mapToDto(updatedOrder);
    }


    private OrderDto mapToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(order, orderDto);
        orderDto.setProductIds(order.getProducts().stream().map(Product::getId).collect(Collectors.toList()));
        orderDto.setUserId(order.getUser().getId());
        return orderDto;
    }
}
