package com.sparta.lecture.global.controller;

import com.sparta.lecture.global.dto.ResponseDto;
import com.sparta.lecture.global.dto.UserRequestDto;
import com.sparta.lecture.global.dto.UserResponseDto;
import com.sparta.lecture.global.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/user/signup")
    public ResponseEntity<ResponseDto<String>> registerUser(@Valid @RequestBody UserRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 유효성 검사 실패 시 에러 메시지 반환
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessage.append(error.getDefaultMessage()).append(". ");
            }
            return ResponseEntity.badRequest().body(new ResponseDto<>(false, errorMessage.toString(), null));
        }

        userService.registerUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto<>(true, "회원가입 성공", null));
    }

    @PostMapping("/api/user/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody UserRequestDto requestDto) {
        UserResponseDto responseDto = userService.login(requestDto.getEmail(), requestDto.getPassword());
        if (responseDto != null && responseDto.isStatus()) {
            return ResponseEntity.ok().body(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
        }
    }
}