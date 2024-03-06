package com.sparta.lecture.domain.lecture.controller;

import com.sparta.lecture.domain.lecture.dto.LectureRequestDto;
import com.sparta.lecture.domain.lecture.dto.LectureResponseDto;
import com.sparta.lecture.domain.lecture.entity.Category;
import com.sparta.lecture.domain.lecture.service.LectureService;
import com.sparta.lecture.global.jwt.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lecture")
public class LectureController {

    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @PostMapping("/")
    public ResponseEntity<LectureResponseDto.CreateLectureResponseDto> createLecture(
            @RequestBody @Valid LectureRequestDto.CreateLectureRequestDto requestDto,
            @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(lectureService.createLecture(requestDto, tokenValue));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LectureResponseDto.GetLectureResponseDto> getLecture(
            @PathVariable Long id,
            @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(lectureService.getLecture(id, tokenValue));
    }

    @GetMapping("/{category}")
    public ResponseEntity<List<LectureResponseDto.GetLectureResponseDto>> getLectureCategory(
            @PathVariable Category category,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(lectureService.getLectureCategory(category, sort, direction, tokenValue));
    }

}
