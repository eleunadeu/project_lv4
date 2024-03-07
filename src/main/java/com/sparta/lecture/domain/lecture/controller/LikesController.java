package com.sparta.lecture.domain.lecture.controller;

import com.sparta.lecture.domain.lecture.service.LikesService;
import com.sparta.lecture.global.jwt.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lecture")
public class LikesController {

    // 보안 및 검증(Secured)
    // 컨트롤러 입력 값 예외 처리
    private final LikesService likesService;

    public LikesController(LikesService likesService) {
        this.likesService = likesService;
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> toggleLike(@PathVariable Long id, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {

        likesService.toggleLike(id, tokenValue);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
