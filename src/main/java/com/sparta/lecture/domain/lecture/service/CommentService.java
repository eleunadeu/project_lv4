package com.sparta.lecture.domain.lecture.service;

import com.sparta.lecture.domain.lecture.dto.CommentDto;
import com.sparta.lecture.domain.lecture.entity.Comment;
import com.sparta.lecture.domain.lecture.entity.Lecture;
import com.sparta.lecture.domain.lecture.repository.CommentRepository;
import com.sparta.lecture.domain.lecture.repository.LectureRepository;
import com.sparta.lecture.global.entity.User;
import com.sparta.lecture.global.handler.exception.CustomApiException;
import com.sparta.lecture.global.jwt.JwtUtil;
import com.sparta.lecture.global.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.lecture.global.handler.exception.ErrorCode.*;

@Slf4j(topic = "comment")
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public CommentService(CommentRepository commentRepository,
                          LectureRepository lectureRepository,
                          UserRepository userRepository,
                          JwtUtil jwtUtil) {
        this.commentRepository = commentRepository;
        this.lectureRepository = lectureRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public CommentDto createComment(Long id, String content, String tokenValue) {
        User user = authenticateUser(tokenValue);

        // 강의 존재 여부 확인
        Lecture lecture = validateLectureExists(id);

        // 사용자 정보와 강의 정보를 포함한 댓글 생성 및 저장
        Comment comment = Comment.builder()
                .content(content)
                .lecture(lecture)
                .user(user)
                .build();
        comment = commentRepository.save(comment);

        // DTO 반환
        return new CommentDto(comment.getCommentId(), comment.getContent(),
                comment.getLecture(), comment.getUser());
    }

    @Transactional(readOnly = true)
    public List<CommentDto.GetCommentResponseDto> getLectureComments(Long id, String tokenValue) {
        Lecture lecture = validateLectureExists(id);

        List<Comment> comments = commentRepository.findByLecture(lecture);

        return comments.stream()
                .map(CommentDto.GetCommentResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto updateLectureComment(Long id, Long commentId, String content, String tokenValue) {
        // 사용자 검증
        Comment comment = validateCommentOwnership(id, commentId, tokenValue);

        // 댓글 수정
        comment.updateComment(content);

        // 수정된 댓글 정보 반환
        return new CommentDto(comment.getCommentId(), comment.getContent(),
                comment.getLecture(), comment.getUser());
    }

    @Transactional
    public void deleteLectureComment(Long id, Long commentId, String tokenValue) {
        // 사용자 검증
        Comment comment = validateCommentOwnership(id, commentId, tokenValue);

        // 댓글 삭제
        commentRepository.delete(comment);
    }

    // 검증 메서드 보안 강화 및, 유틸리티 클래스화 고려
    // 예외 처리 세분화
    // 토큰 유효성 검사 및 사용자 인증을 수행하는 메서드
    private User authenticateUser(String tokenValue) {
        String token = jwtUtil.substringToken(tokenValue);
        if (!jwtUtil.validateToken(token)) {
            throw new CustomApiException(TOKEN_NOT_VALID.getMessage());
        }
        Claims claims = jwtUtil.getUserInfoFromToken(token);
        String email = claims.getSubject();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomApiException(NOT_FOUND_USER_ID.getMessage()));
    }

    // 강의 검증
    private Lecture validateLectureExists(Long id) {
        return lectureRepository.findById(id)
                .orElseThrow(() -> new CustomApiException(NOT_FOUND_LECTURE_INFORMATION.getMessage()));
    }

    // 댓글 사용자 검증
    private Comment validateCommentOwnership(Long lectureId, Long commentId, String tokenValue) {
        User user = authenticateUser(tokenValue);

        validateLectureExists(lectureId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomApiException(NOT_FOUND_COMMENT.getMessage()));

        if (!comment.getUser().equals(user)) {
            throw new CustomApiException(UNAUTHORIZED_COMMENT.getMessage());
        }

        return comment;
    }
}
