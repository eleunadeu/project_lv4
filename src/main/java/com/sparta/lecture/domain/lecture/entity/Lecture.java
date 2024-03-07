package com.sparta.lecture.domain.lecture.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.lecture.domain.tutor.entity.Tutor;
import com.sparta.lecture.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "lecture")
public class Lecture extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String lectureName;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String lectureIntro;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    // lazy 로딩 적용
    @JsonManagedReference
    @OneToMany(mappedBy = "lecture")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Lecture(String lectureName, Integer price,
                   String lectureIntro, Category category,
                   Tutor tutor) {

        this.lectureName = lectureName;
        this.price = price;
        this.lectureIntro = lectureIntro;
        this.category = category;
        this.tutor = tutor;
    }
}
