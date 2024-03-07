package com.sparta.lecture.global.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Builder
    public User(String email, String password, String gender, String phoneNumber, String address, AuthEnum authority) {
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.authority = authority;
    }
}
