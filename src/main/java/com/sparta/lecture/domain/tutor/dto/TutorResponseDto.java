package com.sparta.lecture.domain.tutor.dto;

import lombok.Builder;
import lombok.Getter;

public class TutorResponseDto {
    @Getter
    public static class CreateTutorResponseDto {
        private String name;
        private Integer career;
        private String corp;
        private String phoneNumber;
        private String intro;

        @Builder
        public CreateTutorResponseDto(String name, Integer career, String corp, String phoneNumber, String intro) {
            this.name = name;
            this.career = career;
            this.corp = corp;
            this.phoneNumber = phoneNumber;
            this.intro = intro;
        }
    }

    @Getter
    public static class ReadTutorResponseDto {
        private String name;
        private Integer career;
        private String corp;
        private String intro;

        @Builder
        public ReadTutorResponseDto(String name, Integer career, String corp, String intro) {
            this.name = name;
            this.career = career;
            this.corp = corp;
            this.intro = intro;
        }
    }


}
