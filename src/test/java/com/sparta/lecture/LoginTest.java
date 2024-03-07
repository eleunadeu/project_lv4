package com.sparta.lecture;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.lecture.global.dto.LoginRequestDto;
import com.sparta.lecture.global.dto.SignupRequestDto;
import com.sparta.lecture.global.entity.AuthEnum;
import com.sparta.lecture.global.entity.User;
import com.sparta.lecture.global.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.ModelMap;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원 가입 테스트")
    void signupTest() throws Exception {
        // Given
        SignupRequestDto signupRequestDto = new SignupRequestDto();

//        signupRequestDto.setEmail("test@example.com");
//        signupRequestDto.setPassword("Test1234!"); // 유효한 비밀번호
//        signupRequestDto.setGender("male");
//        signupRequestDto.setPhoneNumber("01012345678");
//        signupRequestDto.setAddress("Seoul");
//        signupRequestDto.setAuthority(AuthEnum.USER);

                // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("success"));

        // Verify
        User user = userRepository.findByEmail("test@example.com").orElse(null);
        assert user != null;
        assert user.getEmail().equals(signupRequestDto.getEmail());
        assert user.getPassword().equals(signupRequestDto.getPassword()); // 비밀번호는 해싱되어 저장되므로 일치하는지만 확인
    }

//    @Test
//    @DisplayName("로그인 테스트")
//    void loginTest() throws Exception {
//        // Given
//        User user = new User();
//        user.setEmail("test@example.com");
//        user.setPassword("$2a$10$xS/nTcgLTTzstbEnit2Ode5gMD4y5cGQY8sf45Bcyw.h7oG/6WeLa"); // 암호화된 패스워드: Test1234!
//        userRepository.save(user);
//
//        LoginRequestDto loginRequestDto = new LoginRequestDto();
//        loginRequestDto.setEmail("test@example.com");
//        loginRequestDto.setPassword("Test1234!"); // 유효한 비밀번호
//
//        // When & Then
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequestDto)))
//                .andExpect(status().isOk())
//                .andExpect(header().exists("Authorization")); // JWT 토큰이 Header에 있는지 확인
//    }
}