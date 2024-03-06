package com.sparta.lecture;

import com.sparta.lecture.global.dto.UserRequestDto;
import com.sparta.lecture.global.entity.AuthEnum;
import com.sparta.lecture.global.entity.User;
import com.sparta.lecture.global.jwt.JwtUtil;
import com.sparta.lecture.global.repository.UserRepository;
import com.sparta.lecture.global.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
@SpringBootTest
@AutoConfigureMockMvc
class LectureApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
    }


    @Test
    @WithMockUser(roles = "USER")
    void testRegisterUser() throws Exception {
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("qQew1230!");
        requestDto.setGender("male");
        requestDto.setPhoneNumber("1234567890");
        requestDto.setAddress("Test Address");
        requestDto.setAuthority(AuthEnum.USER.getAuthority());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@example.com\", \"password\": \"qQew1230!\", \"gender\": \"male\", \"phoneNumber\": \"1234567890\", \"address\": \"Test Address\", \"authority\": \"USER\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testLogin() throws Exception {
        // 유효한 사용자로 로그인
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"valid_user@example.com\", \"password\": \"valid_password\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // 회원 가입 요청
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@example.com\", \"password\": \"qQew1230!\", \"gender\": \"male\", \"phoneNumber\": \"1234567890\", \"address\": \"Test Address\", \"authority\": \"USER\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);
        when(jwtUtil.createToken("test@example.com", AuthEnum.USER)).thenReturn("test_token");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@example.com\", \"password\": \"qQew1230!\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("로그인 성공"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("test_token"));
    }
}