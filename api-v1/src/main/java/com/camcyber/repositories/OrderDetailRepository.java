package com.camcyber.repositories;

import com.camcyber.entities.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity,Integer> {
}
