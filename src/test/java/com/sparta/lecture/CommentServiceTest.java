package com.sparta.lecture;

import com.sparta.lecture.domain.lecture.dto.CommentDto;
import com.sparta.lecture.domain.lecture.entity.Comment;
import com.sparta.lecture.domain.lecture.entity.Lecture;
import com.sparta.lecture.domain.lecture.repository.CommentRepository;
import com.sparta.lecture.domain.lecture.repository.LectureRepository;
import com.sparta.lecture.domain.lecture.service.CommentService;
import com.sparta.lecture.global.entity.User;
import com.sparta.lecture.global.jwt.JwtUtil;
import com.sparta.lecture.global.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private CommentService commentService;

    private final Long lectureId = 1L;
    private final Long commentId = 1L;
    private final String content = "Test Comment";
    private final String tokenValue = "Bearer token";

    @BeforeEach
    void setUp() {
        Claims claims = mockClaims(); // mockClaims() 메서드 호출
        when(jwtUtil.substringToken(anyString())).thenReturn("token");
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.getUserInfoFromToken(anyString())).thenReturn(claims); // 반환값 설정이 필요함
    }

    private Claims mockClaims() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("username");
        return claims;
    }

    @Test
    @DisplayName("댓글 생성 테스트")
    void createCommentTest() {
        // Given
        User user = new User();
        Lecture lecture = new Lecture();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(lecture));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        CommentDto createdComment = commentService.createComment(lectureId, content, tokenValue);

        // Then
        assertNotNull(createdComment);
        assertEquals(content, createdComment.getContent());
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    void updateLectureCommentTest() {
        // Given
        User user = new User();
        Lecture lecture = new Lecture();
        Comment comment = new Comment(content, lecture, user);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(lecture));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        // When
        CommentDto updatedComment = commentService.updateLectureComment(lectureId, commentId, content, tokenValue);

        // Then
        assertNotNull(updatedComment);
        assertEquals(content, updatedComment.getContent());
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void deleteLectureCommentTest() {
        // Given
        User user = new User();
        Lecture lecture = new Lecture();
        Comment comment = new Comment(content, lecture, user);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(lecture));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        // When
        assertDoesNotThrow(() -> commentService.deleteLectureComment(lectureId, commentId, tokenValue));

        // Then
        verify(commentRepository, times(1)).delete(comment);
    }
}
