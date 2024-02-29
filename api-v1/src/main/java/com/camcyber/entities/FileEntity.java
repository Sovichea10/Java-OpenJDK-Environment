package com.camcyber.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(schema = "public",name = "file")
@Data
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String code;
}
