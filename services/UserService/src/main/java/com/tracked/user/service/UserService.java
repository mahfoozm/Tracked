package com.tracked.user.service;

<<<<<<< Updated upstream
import com.tracked.user.dtos.UpdateUserDto;
import com.tracked.user.model.User;
import com.tracked.user.repository.UserRepository;
=======
import com.tracked.event.user.UserEvent;
import com.tracked.kafka.config.TrackedKafkaTopic;
import com.tracked.user.model.User;
import com.tracked.user.repository.UserRepository;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
>>>>>>> Stashed changes
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final KafkaTemplate<Integer, UserEvent> kafkaTemplate;

    public UserService(UserRepository userRepository, KafkaTemplate<Integer, UserEvent> kafkaTemplate) {
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

<<<<<<< Updated upstream
    public User updateUser(UpdateUserDto dto) {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(currentEmail)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());

        return userRepository.save(user);
=======
    public User updateUser(String fullName, String email, MultipartFile profileImage) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setFullName(fullName.trim());
        user.setEmail(email.trim());
        user.setUpdatedAt(new Date());

        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String uploadDir = "/app/uploads/profile-images";
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String fileName = "user_" + user.getId() + "_" + profileImage.getOriginalFilename();
                String filePath = Paths.get(uploadDir, fileName).toString();

                File targetFile = new File(filePath);
                try (OutputStream os = new FileOutputStream(targetFile)) {
                    os.write(profileImage.getBytes());
                }

                user.setProfileImageFilename(filePath);
            } catch (Exception e) {
                throw new RuntimeException("Failed to save profile image: " + e.getMessage());
            }
        }

        User updatedUser = userRepository.save(user);

        UserEvent event = new UserEvent(
                updatedUser.getId(),
                updatedUser.getFullName(),
                updatedUser.getEmail(),
                updatedUser.getCreatedAt(),
                updatedUser.getUpdatedAt()
        );

        kafkaTemplate.send(new ProducerRecord<>(TrackedKafkaTopic.USER_TOPIC, updatedUser.getId(), event));
        return updatedUser;
>>>>>>> Stashed changes
    }
}
