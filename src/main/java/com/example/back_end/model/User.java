package com.example.back_end.model;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    private String name;
    @Column(unique = true)
    private String email;
    private String address;
    @Column(unique = true)
    private String phoneNumber;
    private String avatar;
    private boolean status;
    @ManyToOne(targetEntity = Role.class)
    private Role role;


}
