package com.sparta.lecture.global.service;

import com.sparta.lecture.global.dto.UserRequestDto;
import com.sparta.lecture.global.dto.UserResponseDto;
import com.sparta.lecture.global.entity.AuthEnum;
import com.sparta.lecture.global.entity.User;
import com.sparta.lecture.global.jwt.JwtUtil;
import com.sparta.lecture.global.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void registerUser(UserRequestDto requestDto) {
        // 회원가입 로직 구현
        User newUser = new User();
        newUser.setEmail(requestDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(requestDto.getPassword())); // 비밀번호 인코딩
        newUser.setGender(requestDto.getGender());
        newUser.setPhoneNumber(requestDto.getPhoneNumber());
        newUser.setAddress(requestDto.getAddress());
        newUser.setAuthority(AuthEnum.valueOf(requestDto.getAuthority()));

        // 권한 설정
        if (requestDto.getAuthority() != null && requestDto.getAuthority().equals(AuthEnum.Authority.ADMIN)) {
            newUser.setAuthority(AuthEnum.ADMIN);
        } else {
            newUser.setAuthority(AuthEnum.USER);
        }
    }

        public UserResponseDto login (String email, String password){
            // 로그인 로직 구현
            User user = userRepository.findByEmail(email);
            if (user != null && passwordEncoder.matches(password, user.getPassword())) {
                String token = jwtUtil.createToken(email, user.getAuthority()); // JWT 토큰 생성
                return new UserResponseDto(true, "로그인 성공", token);
            } else {
                return new UserResponseDto(false, "이메일 또는 비밀번호가 올바르지 않습니다.", null);
            }
        }
    }

//    public boolean validateToken(String token) {
//        // JWT 토큰 검증
//        return jwtUtil.validateToken(token);
//    }
//
//    public String getEmailFromToken(String token) {
//        // JWT 토큰에서 사용자 이메일 추출
//        return jwtUtil.getEmailFromToken(token);
//    }