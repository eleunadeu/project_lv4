package com.sparta.lecture.domain.lecture.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.lecture.domain.lecture.entity.Category;
import com.sparta.lecture.domain.lecture.entity.Lecture;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class LectureResponseDto {

    @Getter
    @NoArgsConstructor
    public static class CreateLectureResponseDto {
        private String lectureName;
        private Integer price;
        private String lectureIntro;
        private Category category;
        private Long tutor;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;

        public CreateLectureResponseDto(Lecture lecture) {
            this.lectureName = lecture.getLectureName();
            this.price = lecture.getPrice();
            this.lectureIntro = lecture.getLectureIntro();
            this.category = lecture.getCategory();
            this.tutor = lecture.getTutor();
            this.createdAt = lecture.getCreatedAt();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class GetLectureResponseDto {
        private Long id;
        private String lectureName;
        private Integer price;
        private String lectureIntro;
        private Category category;
        private Long tutor;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;

        public GetLectureResponseDto(Lecture lecture) {
            this.id = lecture.getId();
            this.lectureName = lecture.getLectureName();
            this.price = lecture.getPrice();
            this.lectureIntro = lecture.getLectureIntro();
            this.category = lecture.getCategory();
            this.tutor = lecture.getTutor();
            this.createdAt = lecture.getCreatedAt();
        }
    }
}
