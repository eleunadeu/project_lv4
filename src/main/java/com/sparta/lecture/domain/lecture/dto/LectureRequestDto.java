package com.sparta.lecture.domain.lecture.dto;

import com.sparta.lecture.domain.lecture.entity.Category;
import com.sparta.lecture.domain.lecture.entity.Lecture;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LectureRequestDto {

    @Getter
    @NoArgsConstructor
    public static class CreateLectureRequestDto {
        private String lectureName;
        private Integer price;
        private String lectureIntro;
        private Category category;
        private Long tutor;

        public Lecture toEntity() {
            return Lecture.builder()
                    .lectureName(this.lectureName)
                    .price(this.price)
                    .lectureIntro(this.lectureIntro)
                    .category(this.category)
                    .tutor(this.tutor)
                    .category(this.category)
                    .build();
        }
    }
}
