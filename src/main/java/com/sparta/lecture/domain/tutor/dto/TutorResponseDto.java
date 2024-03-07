package com.sparta.lecture.domain.tutor.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public class TutorResponseDto {
    // 공통 필드 추출
    // 필드 검증?
    @Getter
    public static class CreateTutorResponseDto {
        @NotBlank
        private String name;
        @Min(0)
        private Integer career;
        @NotNull
        private String corp;
        @NotNull
        private String phoneNumber;
        @NotNull
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
