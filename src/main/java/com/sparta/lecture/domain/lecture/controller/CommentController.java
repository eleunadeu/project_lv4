package com.sparta.lecture.domain.lecture.controller;

import com.sparta.lecture.domain.lecture.dto.CommentDto;
import com.sparta.lecture.domain.lecture.service.CommentService;
import com.sparta.lecture.global.jwt.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lecture")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 내용 유효성 검사 (DTO 타입으로 받기)
    // 보안 및 권한 권리(컨트롤러 단에서 -> 서비스 단에서 처리하는데 컨트롤러 선에서 해결되도록)
    // 예외 처리
    @PostMapping("/{id}/comment")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long id,
            @RequestBody String content,
            @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createComment(id, content, tokenValue));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDto.GetCommentResponseDto>> getLectureComments(
            @PathVariable Long id,
            @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getLectureComments(id, tokenValue));
    }

    @PutMapping("/{id}/comment/{commentId}")
    public ResponseEntity<CommentDto> updateLectureComment(
            @PathVariable Long id,
            @PathVariable Long commentId,
            @RequestBody String content,
            @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.updateLectureComment(id, commentId, content, tokenValue));
    }

    @DeleteMapping("/{id}/comment/{commentId}")
    public ResponseEntity<Void> deleteLectureComment(
            @PathVariable Long id,
            @PathVariable Long commentId,
            @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {

        commentService.deleteLectureComment(id, commentId, tokenValue);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
