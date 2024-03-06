package com.sparta.lecture.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDto {
    private boolean status;
    private String message;
    private String token; // JWT token
}
