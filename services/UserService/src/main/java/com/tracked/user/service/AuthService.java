package com.tracked.user.service;

import com.tracked.kafka.config.TrackedKafkaTopic;
import com.tracked.event.user.UserEvent;
import com.tracked.user.dtos.LoginDto;
import com.tracked.user.dtos.RegisterDto;
import com.tracked.user.model.User;
import com.tracked.user.repository.UserRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final KafkaTemplate<Integer, UserEvent> kafkaTemplate;

    public AuthService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            KafkaTemplate<Integer, UserEvent> kafkaTemplate
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.kafkaTemplate = kafkaTemplate;
    }

    public User signup(RegisterDto input) {
        User user = new User()
                .setFullName(input.getFullName())
                .setEmail(input.getEmail())
                .setPassword(passwordEncoder.encode(input.getPassword()));

        user = userRepository.save(user);

        this.kafkaTemplate.send(
            TrackedKafkaTopic.USER_TOPIC,
            user.getId(),
            new UserEvent(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
            )
        );

        return user;
    }

    public User authenticate(LoginDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}