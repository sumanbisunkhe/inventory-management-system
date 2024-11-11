package com.example.inventory.repo;

import com.example.inventory.model.Order;
import com.example.inventory.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    boolean existsByOrderDateAndUser(LocalDateTime orderDate, User user);
}
