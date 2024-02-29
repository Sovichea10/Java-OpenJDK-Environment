package com.camcyber.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(schema = "public",name = "products_type")
@Data
public class ProductsTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "productsType")
    private List<ProductEntity> products;

    @OneToOne
    @JoinColumn(name = "icon")
    private FileEntity icon;

}
