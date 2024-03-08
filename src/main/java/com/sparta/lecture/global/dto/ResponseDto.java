package com.sparta.lecture.global.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ResponseDto<T> {
    private boolean status;
    private String message;
    private T data;

    @Builder
    public ResponseDto(boolean status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
