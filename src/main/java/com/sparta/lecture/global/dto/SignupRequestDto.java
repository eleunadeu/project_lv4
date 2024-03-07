package com.sparta.lecture.global.dto;

import com.sparta.lecture.global.entity.AuthEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    @Email
    @NotBlank
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$")
    @NotBlank
    private String password;
    private String gender;
    private String phoneNumber;
    private String address;
    private AuthEnum authority;
    private String authToken;
}
