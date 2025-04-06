package com.tracked.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracked.user.config.TestSecurityConfig;
import com.tracked.user.model.User;
import com.tracked.user.service.JwtService;
import com.tracked.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
            .id(1)
            .fullName("Test User")
            .email("test@example.com")
            .build();


        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(sampleUser, null, sampleUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void shouldReturnAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/users/me"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.fullName").value("Test User"));
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        when(userService.allUsers()).thenReturn(Collections.singletonList(sampleUser));

        mockMvc.perform(get("/users/"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }
}
