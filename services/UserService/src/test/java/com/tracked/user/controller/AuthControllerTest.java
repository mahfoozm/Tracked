package com.tracked.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracked.user.config.TestSecurityConfig;
import com.tracked.user.dtos.LoginDto;
import com.tracked.user.dtos.RegisterDto;
import com.tracked.user.model.User;
import com.tracked.user.service.AuthService;
import com.tracked.user.service.JwtService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegisterUser() throws Exception {
        RegisterDto dto = new RegisterDto();
        dto.setFullName("Jane Doe");
        dto.setEmail("jane@example.com");
        dto.setPassword("password123");
        User user = User.builder().id(1).fullName("Jane Doe").email("jane@example.com").build();

        when(authService.signup(any())).thenReturn(user);

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fullName").value("Jane Doe"))
            .andExpect(jsonPath("$.email").value("jane@example.com"));
    }

    @Test
    void shouldAuthenticateUser() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("john@example.com");
        loginDto.setPassword("secret");
        User user = User.builder().id(1).fullName("John").email("john@example.com").build();

        when(authService.authenticate(any())).thenReturn(user);
        when(jwtService.generateToken(any())).thenReturn("fake-jwt-token");
        when(jwtService.getExpirationTime()).thenReturn(3600L);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("fake-jwt-token"))
            .andExpect(jsonPath("$.expiresIn").value(3600));
    }

    @Test
    void shouldHandleProxyAuth() throws Exception {
        mockMvc.perform(post("/auth"))
            .andExpect(status().isNoContent());
    }
}
