package com.sparta.lecture.global.service;

import com.sparta.lecture.global.dto.ResponseDto;
import com.sparta.lecture.global.dto.SignupRequestDto;
import com.sparta.lecture.global.entity.AuthEnum;
import com.sparta.lecture.global.entity.User;
import com.sparta.lecture.global.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "gASLfqXtNROJDYcEujnmQbMkWTzvZP";
    public ResponseDto<Object> signup(SignupRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUser = userRepository.findByEmail(email);
        if (checkUser.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 사용자 권한 확인
        AuthEnum auth = AuthEnum.USER;
        if (requestDto.getAuthority().equals(AuthEnum.ADMIN)) {
            if (!ADMIN_TOKEN.equals(requestDto.getAuthToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            auth = AuthEnum.ADMIN;
        }

        // 사용자 등록
        User user = User.builder()
                .email(email)
                .password(password)
                .authority(auth)
                .address(requestDto.getAddress())
                .phoneNumber(requestDto.getPhoneNumber())
                .gender(requestDto.getGender())
                .build();
        userRepository.save(user);

        return new ResponseDto<>(true, "회원가입 성공", null);
    }
}
