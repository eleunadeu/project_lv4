package com.sparta.lecture.global.service;

import com.sparta.lecture.global.dto.LoginRequestDto;
import com.sparta.lecture.global.dto.SignupRequestDto;
import com.sparta.lecture.global.entity.AuthEnum;
import com.sparta.lecture.global.entity.User;
import com.sparta.lecture.global.jwt.JwtUtil;
import com.sparta.lecture.global.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    // 관리자 회원 가입 인가 방법  -- ADMIN_TOKEN 선언
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    // 회원가입
    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();
        String password = signupRequestDto.getPassword();
        String address = signupRequestDto.getAddress();
        String gender = signupRequestDto.getGender();
        String phoneNumber = signupRequestDto.getPhoneNumber();


        // 회원 중복 확인
        Optional<User> found = userRepository.findByEmail(email);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }
        // 사용자 ROLE 확인 (관리자인 경우, ADMIN / 사옹자인 경우, USER 부여)
        AuthEnum role = AuthEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = AuthEnum.ADMIN;
        }
        User user = new User(email, password, gender, phoneNumber, address, role);
        userRepository.save(user);
    }

    // 로그인
    @Transactional
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );
        // 비밀번호 확인
        if(!user.getPassword().equals(password)){
            throw  new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 토큰을 쿠키에 담아서 보내는 방법과 responses header에 담아서 보내는 방법이 있는데, 여기선 response header에 담아서 보냄
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getEmail(), user.getRole()));
    }
}
