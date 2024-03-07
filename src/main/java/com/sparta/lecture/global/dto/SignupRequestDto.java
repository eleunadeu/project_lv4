package com.sparta.lecture.global.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequestDto {
    private String email;
    private String password;
    private String gender;
    private String phoneNumber;
    private String address;
    private boolean admin = false;
    private String adminToken = "";
}
