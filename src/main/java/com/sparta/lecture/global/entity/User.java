package com.sparta.lecture.global.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;

@Entity
@Getter
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String gender;

    @Column
    private String phoneNumber;

    @Column
    private String address;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AuthEnum authority;

}
