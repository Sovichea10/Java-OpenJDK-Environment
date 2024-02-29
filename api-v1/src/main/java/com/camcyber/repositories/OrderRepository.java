package com.camcyber.repositories;

import com.camcyber.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity,Integer> {
    List<OrderEntity> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
