package com.camcyber.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(schema = "public",name = "order_details")
@Data
public class OrderDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int quantity;
    @Column(name = "unit_price")
    private double price;
    private int discount;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

}
