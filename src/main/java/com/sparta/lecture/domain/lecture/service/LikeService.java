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
        // 토큰 검증
        isExpiredToken(jwtUtil.substringToken(tokenValue));

        // 사용자 검증
        Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.substringToken(tokenValue));
        String username = claims.getSubject(); // 또는 getId() 대신 getSubject()를 사용, 상황에 따라 다름

        User user = (User) userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomApiException("사용자가 존재하지 않습니다."));

        // 강의 검증
        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("강의가 존재하지 않습니다."));

        // 좋아요
        likeRepository.findByLectureAndUser(lecture, user).ifPresentOrElse(
                like -> likeRepository.delete((Like) like), // 이미 좋아요가 있다면 삭제
                () -> likeRepository.save(new Like(lecture, user)) // 없다면 추가
        );
    }

    // 토큰 검증
    private void isExpiredToken(String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }
    }
}
