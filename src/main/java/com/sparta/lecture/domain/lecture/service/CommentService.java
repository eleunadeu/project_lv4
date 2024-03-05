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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public CommentDto createComment(Long id, String content, String tokenValue) {
        isExpiredToken(jwtUtil.substringToken(tokenValue));

        // 토큰에서 사용자 정보 추출 및 사용자 엔터티 조회
        Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.substringToken(tokenValue));
        String username = claims.getId();

        User user = (User) userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomApiException("사용자가 존재하지 않습니다."));

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

    public List<CommentDto.GetCommentResponseDto> getLectureComments(Long id, String tokenValue) {
        isExpiredToken(jwtUtil.substringToken(tokenValue));

        Lecture lecture = validateLectureExists(id);

        List<Comment> comments = commentRepository.findByLecture(lecture);

        return comments.stream()
                .map(CommentDto.GetCommentResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto updateLectureComment(Long id, Long commentId, String content, String tokenValue) {
        isExpiredToken(jwtUtil.substringToken(tokenValue));

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
        isExpiredToken(jwtUtil.substringToken(tokenValue));

        // 사용자 검증
        Comment comment = validateCommentOwnership(id, commentId, tokenValue);

        // 댓글 삭제
        commentRepository.delete(comment);
    }

    // 토큰 검증
    private void isExpiredToken(String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }
    }

    // 강의 검증
    public Lecture validateLectureExists(Long id) {
        return lectureRepository.findById(id)
                .orElseThrow(() -> new CustomApiException("해당 강의가 존재하지 않습니다."));
    }

    // 댓글 사용자 검증
    @Transactional
    public Comment validateCommentOwnership(Long lectureId, Long commentId, String tokenValue) {
        Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.substringToken(tokenValue));
        String username = claims.getSubject(); // 또는 getId() 대신 getSubject()를 사용, 상황에 따라 다름

        User user = (User) userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomApiException("사용자가 존재하지 않습니다."));

        lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강의가 존재하지 않습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        if (!comment.getUser().equals(user)) {
            throw new IllegalArgumentException("댓글에 대한 권한이 없습니다.");
        }

        return comment;
    }
}
