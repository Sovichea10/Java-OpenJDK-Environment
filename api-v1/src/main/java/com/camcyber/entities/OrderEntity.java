package com.camcyber.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(schema = "public",name = "order")
@Data
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "receipt_number")
    private String receiptNumber;
    @Column(name = "total_price")
    private double totalPrice;
    private int discount;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "cashier_id")
    private UserEntity user;

    @OneToOne(mappedBy = "order")
    OrderDetailEntity orderDetail;
}
