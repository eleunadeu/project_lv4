package com.sparta.lecture.domain.tutor.dto;

import com.sparta.lecture.domain.tutor.entity.Tutor;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

public class TutorRequestDto {

    @Getter
    public static class CreateTutorRequestDto {
        @NotBlank(message = "강사 이름을 입력하세요.")
        private String name;

        private Integer career;
        private String corp;
        private String phoneNumber;
        private String intro;

        @Builder
        public CreateTutorRequestDto(String name, Integer career, String corp, String phoneNumber, String intro) {
            this.name = name;
            this.career = career;
            this.corp = corp;
            this.phoneNumber = phoneNumber;
            this.intro = intro;
        }

        public Tutor toEntity() {
            return Tutor.builder()
                    .name(this.name)
                    .career(this.career)
                    .corp(this.corp)
                    .phoneNumber(this.phoneNumber)
                    .intro(this.intro)
                    .build();
        }
    }
}
