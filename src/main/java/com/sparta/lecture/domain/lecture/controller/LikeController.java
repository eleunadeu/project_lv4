package com.sparta.lecture.domain.lecture.controller;

import com.sparta.lecture.domain.lecture.service.LikeService;
import com.sparta.lecture.global.jwt.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lecture")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> toggleLike(@PathVariable Long id, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {

        likeService.toggleLike(id, tokenValue);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
