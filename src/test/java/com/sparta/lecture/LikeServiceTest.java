package com.sparta.lecture;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.sparta.lecture.domain.lecture.entity.Lecture;
import com.sparta.lecture.domain.lecture.entity.Like;
import com.sparta.lecture.domain.lecture.repository.LectureRepository;
import com.sparta.lecture.domain.lecture.repository.LikeRepository;
import com.sparta.lecture.domain.lecture.service.LikeService;
import com.sparta.lecture.global.entity.User;
import com.sparta.lecture.global.handler.exception.CustomApiException;
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

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private LikeService likeService;

    private User user;
    private Lecture lecture;
    private Like like;
    private String token;
    private Claims claims;

    @BeforeEach
    void setUp() {
        user = new User(); // 필요한 속성 설정
        lecture = new Lecture(); // 필요한 속성 설정
        like = new Like(lecture, user);
        token = "Bearer token";
        claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("username");
    }

    @Test
    @DisplayName("존재하는 좋아요를 삭제")
    void toggleLike_LikeExists_DeletesLike() {
        when(jwtUtil.substringToken(token)).thenReturn("token");
        when(jwtUtil.getUserInfoFromToken("token")).thenReturn(claims);
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(lecture));
        when(likeRepository.findByLectureAndUser(lecture, user)).thenReturn(Optional.of(like));

        likeService.toggleLike(1L, token);

        verify(likeRepository).delete(like);
    }

    @Test
    @DisplayName("존재하지 않는 좋아요를 저장")
    void toggleLike_LikeDoesNotExist_SavesLike() {
        when(jwtUtil.substringToken(token)).thenReturn("token");
        when(jwtUtil.getUserInfoFromToken("token")).thenReturn(claims);
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(lecture));
        when(likeRepository.findByLectureAndUser(lecture, user)).thenReturn(Optional.empty());

        likeService.toggleLike(1L, token);

        verify(likeRepository).save(any(Like.class));
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 사용자 인증 실패")
    void authenticateUser_InvalidToken_ThrowsCustomApiException() {
        when(jwtUtil.substringToken(token)).thenReturn("token");
        when(jwtUtil.getUserInfoFromToken("token")).thenReturn(null);

        assertThrows(CustomApiException.class, () -> likeService.authenticateUser(token));
    }
}