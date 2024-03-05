package com.sparta.lecture.domain.tutor.controller;

import com.sparta.lecture.domain.tutor.dto.TutorRequestDto;
import com.sparta.lecture.domain.tutor.dto.TutorResponseDto;
import com.sparta.lecture.domain.tutor.service.TutorService;
import com.sparta.lecture.global.jwt.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tutor")
public class TutorController {

    private final TutorService tutorService;
    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @PostMapping("/")
    public ResponseEntity<TutorResponseDto.CreateTutorResponseDto> createTutor(
            @RequestBody @Valid TutorRequestDto.CreateTutorRequestDto requestDto,
            @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tutorService.createTutor(requestDto, tokenValue));
    }
}
