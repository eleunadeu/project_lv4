package com.sparta.lecture.domain.lecture.service;

import com.sparta.lecture.domain.lecture.dto.LectureRequestDto;
import com.sparta.lecture.domain.lecture.dto.LectureResponseDto;
import com.sparta.lecture.domain.lecture.entity.Category;
import com.sparta.lecture.domain.lecture.entity.Lecture;
import com.sparta.lecture.domain.lecture.repository.LectureRepository;
import com.sparta.lecture.domain.lecture.repository.LikesRepository;
import com.sparta.lecture.domain.tutor.entity.Tutor;
import com.sparta.lecture.domain.tutor.repository.TutorRepository;
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

import static com.sparta.lecture.global.handler.exception.ErrorCode.*;

@Service
public class LectureService {

    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;
    private final LikesRepository likesRepository;
    private final TutorRepository tutorRepository;
    private final JwtUtil jwtUtil;
    public LectureService(LectureRepository lectureRepository, UserRepository userRepository,
                          LikesRepository likesRepository, TutorRepository tutorRepository, JwtUtil jwtUtil) {
        this.lectureRepository = lectureRepository;
        this.userRepository = userRepository;
        this.likesRepository = likesRepository;
        this.tutorRepository = tutorRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public LectureResponseDto.CreateLectureResponseDto createLecture(
            LectureRequestDto.CreateLectureRequestDto requestDto, String tokenValue) {

        validateAndAuthenticateToken(tokenValue);

        // Tutor 엔티티 조회
        Tutor tutor = tutorRepository.findById(requestDto.getTutorId())
                .orElseThrow(() -> new CustomApiException("Tutor not found"));

        // toEntity 메서드를 수정하여 Tutor 엔티티를 전달
        Lecture lecture = lectureRepository.save(requestDto.toEntity(tutor));

        return new LectureResponseDto.CreateLectureResponseDto(lecture);
    }

    @Transactional(readOnly = true)
    public LectureResponseDto.GetLectureResponseDto getLecture(Long id, String tokenValue) {
        validateAndAuthenticateToken(tokenValue);

        // 강의 검증
        Lecture lecture = lectureRepository.findById(id).orElseThrow(
                () -> new CustomApiException(NOT_FOUND_CATEGORY_ID.getMessage()));

        User user = authenticateUser(tokenValue);

        // 강의에 대한 좋아요 상태 확인
        boolean liked = isLiked(lecture, user.getId());

        return new LectureResponseDto.GetLectureResponseDto(lecture, liked);
    }

    // 정렬 방향 및, 카테고리에 대한 유효성 검사 강화
    @Transactional(readOnly = true)
    public List<LectureResponseDto.GetLectureResponseDto> getLectureCategory(
            Category category, String sort, String direction, String tokenValue) {

        validateAndAuthenticateToken(tokenValue);

        User user = authenticateUser(tokenValue);

        // 정렬 방향 유효성 검사 및 기본값 설정
        // 'asc' 및 유효하지 않은 값에 대해 기본적으로 오름차순 정렬을 적용
        Sort sortDirection = ("desc".equalsIgnoreCase(direction))
                ? Sort.by(sort).descending() : Sort.by(sort).ascending();

        List<Lecture> lectures = lectureRepository.findByCategory(category, sortDirection);

        return lectures.stream().map(lecture -> {
            boolean liked = likesRepository.existsByLectureAndUser(lecture, user);
            return new LectureResponseDto.GetLectureResponseDto(lecture, liked);
        }).collect(Collectors.toList());
    }

    // 토큰 검증
    private void validateAndAuthenticateToken(String tokenValue) {
        String token = jwtUtil.substringToken(tokenValue);
        if (!jwtUtil.validateToken(token)) {
            throw new CustomApiException(TOKEN_NOT_VALID.getMessage());
        }
    }

    public boolean isLiked(Lecture lecture, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        return likesRepository.existsByLectureAndUser(lecture, user);
    }

    public User authenticateUser(String tokenValue) {
        // 토큰에서 사용자 정보 추출
        Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.substringToken(tokenValue));
        String email = claims.getSubject(); // "getSubject()"는 사용자 이름 또는 고유 식별자를 의미

        // 사용자 검증 및 반환
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomApiException(BAD_REQUEST_USER_AUTHENTICATION.getMessage()));

    }
}
