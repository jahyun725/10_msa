package com.my.userservice.command.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private com.my.userservice.command.entity.UserRole role = com.my.userservice.command.entity.UserRole.USER;

    public void setEncodedPassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
