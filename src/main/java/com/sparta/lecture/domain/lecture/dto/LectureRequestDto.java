package com.sparta.lecture.domain.lecture.dto;

import com.sparta.lecture.domain.lecture.entity.Category;
import com.sparta.lecture.domain.lecture.entity.Lecture;
import com.sparta.lecture.domain.tutor.entity.Tutor;
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
        private Long tutorId;

        public Lecture toEntity(Tutor tutor) {
            return Lecture.builder()
                    .lectureName(this.lectureName)
                    .price(this.price)
                    .lectureIntro(this.lectureIntro)
                    .category(this.category)
                    .tutor(tutor)
                    .category(this.category)
                    .build();
        }
    }
}
