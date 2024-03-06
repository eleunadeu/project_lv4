//package com.sparta.lecture;
//
//import com.sparta.lecture.domain.lecture.dto.LectureRequestDto;
//import com.sparta.lecture.domain.lecture.dto.LectureResponseDto;
//import com.sparta.lecture.domain.lecture.entity.Category;
//import com.sparta.lecture.domain.lecture.entity.Lecture;
//import com.sparta.lecture.domain.lecture.repository.LectureRepository;
//import com.sparta.lecture.domain.lecture.repository.LikeRepository;
//import com.sparta.lecture.domain.lecture.service.LectureService;
//import com.sparta.lecture.global.entity.User;
//import com.sparta.lecture.global.jwt.JwtUtil;
//import com.sparta.lecture.global.repository.UserRepository;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Sort;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class LectureServiceTest {
//
//    @Mock
//    private LectureRepository lectureRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private LikeRepository likeRepository;
//
//    @Mock
//    private JwtUtil jwtUtil;
//
//    @InjectMocks
//    private LectureService lectureService;
//
//    @BeforeEach
//    void setUp() {
//        // Mock 객체 초기화 (MockitoExtension에 의해 자동으로 처리됩니다.)
//    }
//
//    @Test
//    @DisplayName("강사 등록 테스트")
//    void createLectureTest() {
//        // Given
//        String tokenValue = "Bearer token";
//        String strippedToken = "token";
//        LectureRequestDto.CreateLectureRequestDto requestDto = new LectureRequestDto.CreateLectureRequestDto();
//        Lecture savedLecture = new Lecture();
//
//        when(jwtUtil.substringToken(tokenValue)).thenReturn(strippedToken);
//        when(jwtUtil.validateToken(strippedToken)).thenReturn(true);
//        when(lectureRepository.save(any(Lecture.class))).thenReturn(savedLecture);
//
//        // When
//        LectureResponseDto.CreateLectureResponseDto result = lectureService.createLecture(requestDto, tokenValue);
//
//        // Then
//        assertNotNull(result);
//        verify(lectureRepository).save(any(Lecture.class));
//    }
//
//    @Test
//    @DisplayName("강의 정보 조회 테스트")
//    void getLectureTest() {
//        // Given
//        Long lectureId = 1L;
//        Long userId = 1L; // 가정된 사용자 ID
//        String tokenValue = "Bearer token";
//        String strippedToken = "token";
//        Lecture lecture = new Lecture();
//        Claims claims = Jwts.claims();
//        claims.setSubject("username");
//        User user = new User(userId); // 생성자를 사용하여 ID를 설정
//
//        when(jwtUtil.substringToken(tokenValue)).thenReturn(strippedToken);
//        when(jwtUtil.validateToken(strippedToken)).thenReturn(true);
//        when(jwtUtil.getUserInfoFromToken(strippedToken)).thenReturn(claims);
//        when(userRepository.findByUsername("username")).thenReturn(java.util.Optional.of(user)); // findByUsername에 대한 모의 설정
//        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user)); // findById에 대한 모의 설정 추가
//        when(lectureRepository.findById(lectureId)).thenReturn(java.util.Optional.of(lecture));
//        when(likeRepository.existsByLectureAndUser(any(Lecture.class), eq(user))).thenReturn(true);
//
//        // When
//        LectureResponseDto.GetLectureResponseDto result = lectureService.getLecture(lectureId, tokenValue);
//
//        // Then
//        assertNotNull(result);
//        assertTrue(result.isLiked());
//        verify(lectureRepository).findById(lectureId);
//        verify(likeRepository).existsByLectureAndUser(any(Lecture.class), eq(user));
//    }
//
//    @Test
//    @DisplayName("카테고리별 강의 조회 테스트")
//    void getLectureCategoryTest() {
//        // Given
//        Category category = Category.SPRING; // 가정된 카테고리
//        String sort = "createdAt";
//        String direction = "asc";
//        String tokenValue = "Bearer validToken"; // 유효한 토큰 값으로 가정
//        User user = new User(1L); // 가정된 사용자
//        List<Lecture> lectures = List.of(new Lecture(), new Lecture()); // 가정된 강의 목록
//
//        // 토큰이 유효함을 반환하기 위한 스텁 설정을 수정합니다.
//        when(jwtUtil.substringToken(tokenValue)).thenReturn("validToken");
//        when(jwtUtil.validateToken("validToken")).thenReturn(true);
//
//        // 실제 사용자 정보를 반영하는 Claims 객체를 명시적으로 구성합니다.
//        Claims claims = Jwts.claims().setSubject("user1"); // 'user1'은 가정된 사용자 이름입니다.
//        when(jwtUtil.getUserInfoFromToken("validToken")).thenReturn(claims);
//        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
//
//        when(lectureRepository.findByCategory(eq(category), any(Sort.class))).thenReturn(lectures);
//        when(likeRepository.existsByLectureAndUser(any(Lecture.class), eq(user))).thenReturn(true);
//
//        // When
//        List<LectureResponseDto.GetLectureResponseDto> results = lectureService.getLectureCategory(
//                category, sort, direction, tokenValue);
//
//        // Then
//        assertFalse(results.isEmpty()); // 결과가 비어있지 않은지 확인
//        assertEquals(lectures.size(), results.size()); // 반환된 목록의 크기가 예상과 일치하는지 확인
//        assertTrue(results.stream().allMatch(LectureResponseDto.GetLectureResponseDto::isLiked)); // 모든 강의가 'liked' 상태인지 확인
//
//        verify(lectureRepository).findByCategory(eq(category), any(Sort.class)); // 카테고리별 강의 조회가 호출되었는지 확인
//    }
//}