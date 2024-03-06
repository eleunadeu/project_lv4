package com.sparta.lecture;

import com.sparta.lecture.domain.tutor.dto.TutorRequestDto;
import com.sparta.lecture.domain.tutor.dto.TutorResponseDto;
import com.sparta.lecture.domain.tutor.entity.Tutor;
import com.sparta.lecture.domain.tutor.repository.TutorRepository;
import com.sparta.lecture.domain.tutor.service.TutorService;
import com.sparta.lecture.global.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TutorServiceTest {

    private TutorRepository tutorRepository;
    private JwtUtil jwtUtil;
    private TutorService tutorService;

    @BeforeEach
    void setUp() {
        // 모의 객체 생성
        tutorRepository = mock(TutorRepository.class);
        jwtUtil = mock(JwtUtil.class);

        // TutorService 인스턴스 생성 및 의존성 주입
        tutorService = new TutorService(tutorRepository, jwtUtil);
    }

    @Test
    @DisplayName("강사 등록")
    void createTutorTest() {
        // JwtUtil 모의 행동 설정
        String token = "Bearer tokenValue";
        String strippedToken = "tokenValue";
        Claims claims = Jwts.claims().setSubject("ADMIN");
        claims.put(JwtUtil.AUTHORIZATION_KEY, "ADMIN");

        when(jwtUtil.substringToken(token)).thenReturn(strippedToken);
        when(jwtUtil.validateToken(strippedToken)).thenReturn(true);
        when(jwtUtil.getUserInfoFromToken(strippedToken)).thenReturn(claims);

        // TutorRepository 모의 행동 설정
        Tutor savedTutor = new Tutor("Tutor Name", 10, "Tutor Corp", "010-1234-5678", "Introduction");
        when(tutorRepository.save(any(Tutor.class))).thenReturn(savedTutor);

        // 준비
        TutorRequestDto.CreateTutorRequestDto requestDto = new TutorRequestDto.CreateTutorRequestDto(
                "Tutor Name", 10, "Tutor Corp", "010-1234-5678", "Introduction"
        );

        // 실행
        TutorResponseDto.CreateTutorResponseDto responseDto = tutorService.createTutor(requestDto, token);

        // 검증
        assertEquals("Tutor Name", responseDto.getName());
        assertEquals(10, responseDto.getCareer());
        assertEquals("Tutor Corp", responseDto.getCorp());
        assertEquals("Introduction", responseDto.getIntro());
        verify(tutorRepository).save(any(Tutor.class)); // tutorRepository.save가 호출되었는지 확인
    }

    @Test
    @DisplayName("강사 정보 조회")
    void readTutorInfoTest() {
        // 준비
        Long tutorId = 1L;
        Tutor tutor = new Tutor(
                "Tutor Name", 10, "Tutor Corp", "010-1234-5678", "Introduction"
        );
        when(tutorRepository.findById(tutorId)).thenReturn(Optional.of(tutor));

        // 실행
        TutorResponseDto.ReadTutorResponseDto responseDto = tutorService.readTutorInfo(tutorId);

        // 검증
        assertEquals("Tutor Name", responseDto.getName());
        assertEquals(10, responseDto.getCareer());
        assertEquals("Tutor Corp", responseDto.getCorp());
        assertEquals("Introduction", responseDto.getIntro());
        verify(tutorRepository).findById(tutorId); // tutorRepository.findById가 호출되었는지 확인
    }
}
