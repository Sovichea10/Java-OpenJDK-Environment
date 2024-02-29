package com.camcyber.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(schema = "public",name = "user")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String phone;
    private String email;
    private String password;
    @Column(name = "password_last_updater")
    private String pwdLastUpdater;
    @Column(name = "password_last_updated_at")
    private LocalDateTime pwdLastUpdatedAt;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "disabled_at")
    private LocalDateTime disabledAt;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserEntity creator;

    @ManyToOne
    @JoinColumn(name = "updater_id")
    private UserEntity updater;

    @ManyToOne
    @JoinColumn(name = "deleter_id")
    private UserEntity deleter;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private UsersTypeEntity usersType;

    @OneToOne
    @JoinColumn(name = "avatar")
    private FileEntity fileEntity;

    @OneToMany(mappedBy = "user")
    private List<OrderEntity> orders;

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add((GrantedAuthority) () -> usersType.getName());
//        return authorities;
//    }
//
//    @Override
//    public String getUsername() {
//        return phone;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }


}
