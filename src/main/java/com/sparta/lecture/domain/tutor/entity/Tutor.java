package com.sparta.lecture.domain.tutor.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tutor")
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer career;

    @Column(nullable = false)
    private String corp;

    @Column(nullable = false)
    private String phoneNumber;

    @Column
    private String intro;

    @Builder
    public Tutor(String name, Integer career, String corp, String phoneNumber, String intro) {
        this.name = name;
        this.career = career;
        this.corp = corp;
        this.phoneNumber = phoneNumber;
        this.intro = intro;
    }

}
