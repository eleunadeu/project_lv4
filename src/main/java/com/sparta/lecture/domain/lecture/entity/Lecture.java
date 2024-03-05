package com.sparta.lecture.domain.lecture.entity;

import com.sparta.lecture.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;
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

    @Column(nullable = false)
    private Long tutor;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "lecutre")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Lecture(String lectureName, Integer price,
                   String lectureIntro, Category category,
                   Long tutor) {

        this.lectureName = lectureName;
        this.price = price;
        this.lectureIntro = lectureIntro;
        this.category = category;
        this.tutor = tutor;
    }
}
