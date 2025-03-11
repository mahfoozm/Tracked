package com.tracked.user.repository;

import com.tracked.event.user.UserEvent;
import com.tracked.kafka.config.TrackedKafkaTopic;
import com.tracked.user.model.User;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDatabasePopulator {
    private static final Logger logger = LoggerFactory.getLogger(UserDatabasePopulator.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final List<User> initialUsers;
    private final KafkaTemplate<Integer, UserEvent> kafkaTemplate;

    @Autowired
    public UserDatabasePopulator(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        KafkaTemplate<Integer, UserEvent> kafkaTemplate
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        User user1 = new User()
                .setFullName("Mohammad")
                .setEmail("mohammadmahfoozpersonal@gmail.com")
                .setPassword(passwordEncoder.encode("test123"));
        this.initialUsers = new ArrayList<>();
        this.initialUsers.add(user1);
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostConstruct
    public void initDatabase() {
        logger.info("Populating users in database");
        for (User user : this.initialUsers) {
            if (!this.userRepository.existsByEmail(user.getEmail())) {
                logger.info("Creating user {}", user.getEmail());
                this.userRepository.save(user);
                logger.info("Created user {}", user.getEmail());
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
                logger.info("Sent user {} to Kafka", user.getEmail());
            }
        }
    }
}