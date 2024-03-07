package com.sparta.lecture.domain.lecture.dto;

import com.sparta.lecture.domain.lecture.entity.Lecture;
import com.sparta.lecture.global.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikesDto {
    private Long likeId;
    private Lecture lecture;
    private User user;

    @Builder
    public LikesDto(Long likeId, Lecture lecture, User user) {
        this.likeId = likeId;
        this.lecture = lecture;
        this.user = user;
    }
}
