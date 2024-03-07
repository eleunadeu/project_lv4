package com.sparta.lecture.domain.lecture.service;

import com.sparta.lecture.domain.lecture.entity.Lecture;
import com.sparta.lecture.domain.lecture.entity.Likes;
import com.sparta.lecture.domain.lecture.repository.LectureRepository;
import com.sparta.lecture.domain.lecture.repository.LikesRepository;
import com.sparta.lecture.global.entity.User;
import com.sparta.lecture.global.handler.exception.CustomApiException;
import com.sparta.lecture.global.jwt.JwtUtil;
import com.sparta.lecture.global.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.lecture.global.handler.exception.ErrorCode.*;

@Service
public class LikesService {

    private final LikesRepository likesRepository;
    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public LikesService(LikesRepository likesRepository, LectureRepository lectureRepository,
                        UserRepository userRepository, JwtUtil jwtUtil) {
        this.likesRepository = likesRepository;
        this.lectureRepository = lectureRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void toggleLike(Long id, String tokenValue) {
        User user = authenticateUser(tokenValue);

        // 강의 검증
        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(() -> new CustomApiException(NOT_FOUND_LECTURE_INFORMATION.getMessage()));

        // 좋아요
        likesRepository.findByLectureAndUser(lecture, user).ifPresentOrElse(
                likesRepository::delete, // 이미 좋아요가 있다면 삭제
                () -> likesRepository.save(new Likes(lecture, user)) // 없다면 추가
        );
    }

    // 사용자 인증 로직의 중복을 제거하기 위한 메서드
    public User authenticateUser(String tokenValue) {
        // 토큰에서 사용자 정보 추출
        Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.substringToken(tokenValue));

        // 사용자 정보가 null인 경우 예외 처리
        if (claims == null || claims.getSubject() == null) {
            throw new CustomApiException(NOT_FOUND_USER_INFO.getMessage());
        }

        String email = claims.getSubject(); // "getSubject()"는 사용자 이름 또는 고유 식별자를 의미

        // 사용자 검증 및 반환
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomApiException(NOT_FOUND_USER_ID.getMessage()));
    }
}
