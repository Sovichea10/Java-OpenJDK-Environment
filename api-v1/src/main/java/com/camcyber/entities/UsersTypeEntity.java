package com.camcyber.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(schema = "public",name = "users_type")
@Data
public class UsersTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "usersType")
    private List<UserEntity> users;

}
