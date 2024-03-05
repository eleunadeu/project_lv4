package com.sparta.lecture.domain.tutor.service;

import com.sparta.lecture.domain.tutor.dto.TutorRequestDto;
import com.sparta.lecture.domain.tutor.dto.TutorResponseDto;
import com.sparta.lecture.domain.tutor.entity.Tutor;
import com.sparta.lecture.domain.tutor.repository.TutorRepository;
import com.sparta.lecture.global.entity.AuthEnum;
import com.sparta.lecture.global.handler.exception.CustomApiException;
import com.sparta.lecture.global.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.lecture.global.handler.exception.ErrorCode.NOT_FOUND_TUTOR_ID;
import static com.sparta.lecture.global.handler.exception.ErrorCode.UNAUTHORIZED_ADMIN;

@Service
public class TutorService {
    private final TutorRepository tutorRepository;
    private final JwtUtil jwtUtil;
    public TutorService(TutorRepository tutorRepository, JwtUtil jwtUtil) {
        this.tutorRepository = tutorRepository;
        this.jwtUtil = jwtUtil;
    }
    public TutorResponseDto.CreateTutorResponseDto createTutor(TutorRequestDto.CreateTutorRequestDto requestDto, String tokenValue) {
        if (!checkAuthority(tokenValue)) {
            throw new CustomApiException(UNAUTHORIZED_ADMIN.getMessage());
        }
        Tutor tutor = tutorRepository.save(requestDto.toEntity());
        return new TutorResponseDto.CreateTutorResponseDto(
                tutor.getName(), tutor.getCareer(), tutor.getCorp(), tutor.getPhoneNumber(), tutor.getIntro());
    }

    @Transactional(readOnly = true)
    public TutorResponseDto.ReadTutorResponseDto readTutorInfo(Long tutorId) {
        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new CustomApiException(NOT_FOUND_TUTOR_ID.getMessage()));

        return new TutorResponseDto.ReadTutorResponseDto(tutor.getName(), tutor.getCareer(), tutor.getCorp(), tutor.getIntro());
    }

    public boolean checkAuthority(String tokenValue) {
        boolean isAdmin = true;
        // JWT 토큰 substring
        String token = jwtUtil.substringToken(tokenValue);

        // 토큰 검증
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }
        //권한이 Manager인지 확인
        Claims info = jwtUtil.getUserInfoFromToken(token);
        String authority = (String) info.get(JwtUtil.AUTHORIZATION_KEY);
        if (!(AuthEnum.valueOf(authority)).equals(AuthEnum.ADMIN)) {
            return !isAdmin;
        }
        return isAdmin;
    }
}
