package com.sparta.lecture.domain.lecture.service;

import com.sparta.lecture.domain.lecture.dto.LectureRequestDto;
import com.sparta.lecture.domain.lecture.dto.LectureResponseDto;
import com.sparta.lecture.domain.lecture.entity.Category;
import com.sparta.lecture.domain.lecture.entity.Lecture;
import com.sparta.lecture.domain.lecture.repository.LectureRepository;
import com.sparta.lecture.global.handler.exception.CustomApiException;
import com.sparta.lecture.global.jwt.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.lecture.global.handler.exception.ErrorCode.NOT_FOUND_CATEGORY_ID;

@Service
public class LectureService {

    private final LectureRepository lectureRepository;
    private final JwtUtil jwtUtil;
    public LectureService(LectureRepository lectureRepository, JwtUtil jwtUtil) {
        this.lectureRepository = lectureRepository;
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

        Lecture lecture = lectureRepository.findById(id).orElseThrow(
                () -> new CustomApiException(NOT_FOUND_CATEGORY_ID.getMessage()));

        return new LectureResponseDto.GetLectureResponseDto(lecture);
    }

    @Transactional(readOnly = true)
    public List<LectureResponseDto.GetLectureResponseDto> getLectureCategory(Category category, String tokenValue) {
        isExpiredToken(jwtUtil.substringToken(tokenValue));

        List<Lecture> lectures = lectureRepository.findByCategory(category);
        return lectures.stream().map(LectureResponseDto.GetLectureResponseDto::new).collect(Collectors.toList());
    }

    // 토큰 검증
    private void isExpiredToken(String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }
    }
}
