package com.sparta.lecture.domain.lecture.service;

import com.sparta.lecture.domain.lecture.entity.Lecture;
import com.sparta.lecture.domain.lecture.entity.Like;
import com.sparta.lecture.domain.lecture.repository.LectureRepository;
import com.sparta.lecture.domain.lecture.repository.LikeRepository;
import com.sparta.lecture.global.entity.User;
import com.sparta.lecture.global.handler.exception.CustomApiException;
import com.sparta.lecture.global.jwt.JwtUtil;
import com.sparta.lecture.global.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public LikeService(LikeRepository likeRepository, LectureRepository lectureRepository,
                       UserRepository userRepository, JwtUtil jwtUtil) {
        this.likeRepository = likeRepository;
        this.lectureRepository = lectureRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void toggleLike(Long id, String tokenValue) {
        User user = authenticateUser(tokenValue);

        // 강의 검증
        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(() -> new CustomApiException("강의가 존재하지 않습니다."));

        // 좋아요
        likeRepository.findByLectureAndUser(lecture, user).ifPresentOrElse(
                likeRepository::delete, // 이미 좋아요가 있다면 삭제
                () -> likeRepository.save(new Like(lecture, user)) // 없다면 추가
        );
    }

    // 사용자 인증 로직의 중복을 제거하기 위한 메서드
    public User authenticateUser(String tokenValue) {
        // 토큰에서 사용자 정보 추출
        Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.substringToken(tokenValue));

        // 사용자 정보가 null인 경우 예외 처리
        if (claims == null || claims.getSubject() == null) {
            throw new CustomApiException("토큰에서 사용자 정보를 추출할 수 없습니다.");
        }

        String username = claims.getSubject(); // "getSubject()"는 사용자 이름 또는 고유 식별자를 의미

        // 사용자 검증 및 반환
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new CustomApiException("사용자가 존재하지 않습니다."));
    }
}
