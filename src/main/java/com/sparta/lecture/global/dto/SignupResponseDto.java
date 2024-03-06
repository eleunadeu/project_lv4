package com.sparta.lecture.global.dto;

import com.sparta.lecture.global.entity.AuthEnum;
import com.sparta.lecture.global.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponseDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

//    @NotBlank
//    private DepartmentEnum department;

    @NotBlank
    private AuthEnum authority;

    //    @NotBlank
//    private String createUserId;
//
//    private boolean admin = false;
    private String adminToken = "";

    public SignupResponseDto(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
//        this.department = user.getDepartment();
        this.authority = user.getAuthority();
    }
}
