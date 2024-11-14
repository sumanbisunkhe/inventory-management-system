package com.example.inventory.service;

import com.example.inventory.dto.OrderDto;
import com.example.inventory.exceptions.OrderNotFoundException;
import com.example.inventory.exceptions.ProductNotFoundException;
import com.example.inventory.model.Order;
import com.example.inventory.model.Product;
import com.example.inventory.model.User;
import com.example.inventory.repo.OrderRepo;
import com.example.inventory.repo.ProductRepo;
import com.example.inventory.repo.UserRepo;
import com.example.inventory.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private ProductRepo productRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder_Success() {
        // Prepare mock data
        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(1L);
        orderDto.setProductIds(Arrays.asList(1L, 2L));

        User user = new User();
        user.setId(1L);

        Product product1 = new Product();
        product1.setId(1L);
        product1.setPrice(BigDecimal.valueOf(100));

        Product product2 = new Product();
        product2.setId(2L);
        product2.setPrice(BigDecimal.valueOf(200));

        Order order = new Order();
        order.setId(1L);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(BigDecimal.valueOf(300));
        order.setProducts(Arrays.asList(product1, product2));
        order.setUser(user);

        // Set up mocks
        when(userRepo.findById(orderDto.getUserId())).thenReturn(Optional.of(user));
        when(productRepo.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepo.findById(2L)).thenReturn(Optional.of(product2));
        when(orderRepo.save(any(Order.class))).thenReturn(order);

        // Test the createOrder method
        OrderDto createdOrderDto = orderService.createOrder(orderDto);

        // Verify and assert
        assertNotNull(createdOrderDto);
        assertEquals(order.getTotalAmount(), createdOrderDto.getTotalAmount());
        assertEquals(order.getProducts().size(), createdOrderDto.getProductIds().size());
        assertEquals(order.getUser().getId(), createdOrderDto.getUserId());

        verify(orderRepo, times(1)).save(any(Order.class));
    }

    @Test
    void testCreateOrder_ProductNotFound() {
        // Prepare mock data
        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(1L);
        orderDto.setProductIds(Arrays.asList(1L, 2L));

        User user = new User();
        user.setId(1L);

        // Set up mocks
        when(userRepo.findById(orderDto.getUserId())).thenReturn(Optional.of(user));
        when(productRepo.findById(1L)).thenReturn(Optional.of(new Product()));
        when(productRepo.findById(2L)).thenReturn(Optional.empty()); // Simulate product not found

        // Test and assert exception
        assertThrows(ProductNotFoundException.class, () -> orderService.createOrder(orderDto));
    }

    @Test
    void testGetOrderById_Success() {
        // Prepare mock data
        Order order = new Order();
        order.setId(1L);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(BigDecimal.valueOf(100));

        Product product = new Product();
        product.setId(1L);
        order.setProducts(List.of(product));

        User user = new User();
        user.setId(1L);
        order.setUser(user);

        // Set up mocks
        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

        // Test getOrderById
        OrderDto orderDto = orderService.getOrderById(1L);

        // Verify and assert
        assertNotNull(orderDto);
        assertEquals(order.getId(), orderDto.getId());
        assertEquals(order.getTotalAmount(), orderDto.getTotalAmount());
        verify(orderRepo, times(1)).findById(1L);
    }

    @Test
    void testGetOrderById_OrderNotFound() {
        // Set up mocks
        when(orderRepo.findById(anyLong())).thenReturn(Optional.empty());

        // Test and assert exception
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(1L));
    }

    @Test
    void testUpdateOrder_Success() {
        // Prepare mock data
        Order existingOrder = new Order();
        existingOrder.setId(1L);

        Product product1 = new Product();
        product1.setId(1L);
        product1.setPrice(BigDecimal.valueOf(100));

        Product product2 = new Product();
        product2.setId(2L);
        product2.setPrice(BigDecimal.valueOf(200));

        existingOrder.setProducts(List.of(product1));
        existingOrder.setTotalAmount(BigDecimal.valueOf(100));

        User user = new User();
        user.setId(1L);
        existingOrder.setUser(user);

        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(1L);
        orderDto.setProductIds(Arrays.asList(1L, 2L));

        // Set up mocks
        when(orderRepo.findById(1L)).thenReturn(Optional.of(existingOrder));
        when(productRepo.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepo.findById(2L)).thenReturn(Optional.of(product2));
        when(userRepo.findById(orderDto.getUserId())).thenReturn(Optional.of(user));
        when(orderRepo.save(any(Order.class))).thenReturn(existingOrder);

        // Test updateOrder
        OrderDto updatedOrderDto = orderService.updateOrder(1L, orderDto);

        // Verify and assert
        assertNotNull(updatedOrderDto);
        assertEquals(orderDto.getProductIds().size(), updatedOrderDto.getProductIds().size());
        assertEquals(BigDecimal.valueOf(300), updatedOrderDto.getTotalAmount());
        verify(orderRepo, times(1)).save(existingOrder);
    }

    @Test
    void testDeleteOrder_Success() {
        // Prepare mock data
        Order order = new Order();
        order.setId(1L);

        // Set up mocks
        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

        // Test deleteOrder
        orderService.deleteOrder(1L);

        // Verify
        verify(orderRepo, times(1)).delete(order);
    }

    @Test
    void testDeleteOrder_OrderNotFound() {
        // Set up mocks
        when(orderRepo.findById(anyLong())).thenReturn(Optional.empty());

        // Test and assert exception
        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(1L));
    }
}
