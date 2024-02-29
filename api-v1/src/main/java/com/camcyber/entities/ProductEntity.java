package com.camcyber.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;
    private String name;
    @Column(name = "unit_price")
    private double unitPrice;
    private int discount;
    private String description;
    private int quantity;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private ProductsTypeEntity productsType;

    @OneToOne
    @JoinColumn(name = "image")
    private FileEntity image;

    @OneToMany(mappedBy = "product")
    private List<OrderDetailEntity> orderDetails;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserEntity creator;

    @ManyToOne
    @JoinColumn(name = "updater_id")
    private UserEntity updater;

    @ManyToOne
    @JoinColumn(name = "deleter_id")
    private UserEntity deleter;
}
