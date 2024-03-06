package com.sparta.lecture.domain.lecture.service;

import com.sparta.lecture.domain.lecture.dto.LectureRequestDto;
import com.sparta.lecture.domain.lecture.dto.LectureResponseDto;
import com.sparta.lecture.domain.lecture.entity.Category;
import com.sparta.lecture.domain.lecture.entity.Lecture;
import com.sparta.lecture.domain.lecture.repository.LectureRepository;
import com.sparta.lecture.domain.lecture.repository.LikeRepository;
import com.sparta.lecture.global.entity.User;
import com.sparta.lecture.global.handler.exception.CustomApiException;
import com.sparta.lecture.global.jwt.JwtUtil;
import com.sparta.lecture.global.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.lecture.global.handler.exception.ErrorCode.NOT_FOUND_CATEGORY_ID;

@Service
public class LectureService {

    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final JwtUtil jwtUtil;
    public LectureService(LectureRepository lectureRepository, UserRepository userRepository,
                          LikeRepository likeRepository, JwtUtil jwtUtil) {
        this.lectureRepository = lectureRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.jwtUtil = jwtUtil;
    }


    public LectureResponseDto.CreateLectureResponseDto createLecture(
            LectureRequestDto.CreateLectureRequestDto requestDto, String tokenValue) {

        String token = jwtUtil.substringToken(tokenValue);
        isExpiredToken(token);

        Lecture lecture = lectureRepository.save(requestDto.toEntity());

        return new LectureResponseDto.CreateLectureResponseDto(lecture);
    }

    @Transactional(readOnly = true)
    public LectureResponseDto.GetLectureResponseDto getLecture(Long id, String tokenValue) {
        isExpiredToken(jwtUtil.substringToken(tokenValue));

        // 강의 검증
        Lecture lecture = lectureRepository.findById(id).orElseThrow(
                () -> new CustomApiException(NOT_FOUND_CATEGORY_ID.getMessage()));

        User user = authenticateUser(tokenValue);

        // 강의에 대한 좋아요 상태 확인
        boolean liked = isLiked(id, user.getId());

        return new LectureResponseDto.GetLectureResponseDto(lecture, liked);
    }

    @Transactional(readOnly = true)
    public List<LectureResponseDto.GetLectureResponseDto> getLectureCategory(
            Category category, String sort, String direction, String tokenValue) {

        isExpiredToken(jwtUtil.substringToken(tokenValue));

        User user = authenticateUser(tokenValue);

        // 정렬 기준 정의
        Sort sortDirection = "asc".equalsIgnoreCase(direction)
                ? Sort.by(sort).ascending() : Sort.by(sort).descending();

        List<Lecture> lectures = lectureRepository.findByCategory(category, sortDirection);

        return lectures.stream().map(lecture -> {
            boolean liked = likeRepository.existsByLectureAndUser(lecture, user);
            return new LectureResponseDto.GetLectureResponseDto(lecture, liked);
        }).collect(Collectors.toList());
    }

    // 토큰 검증
    private void isExpiredToken(String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }
    }

    public boolean isLiked(Long lectureId, Long userId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("강의가 존재하지 않습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        return likeRepository.existsByLectureAndUser(lecture, user);
    }

    public User authenticateUser(String tokenValue) {
        // 토큰에서 사용자 정보 추출
        Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.substringToken(tokenValue));
        String username = claims.getSubject(); // "getSubject()"는 사용자 이름 또는 고유 식별자를 의미

        // 사용자 검증 및 반환
        return (User) userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomApiException("사용자가 존재하지 않습니다."));
    }
}
