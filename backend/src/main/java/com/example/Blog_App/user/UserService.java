package com.example.Blog_App.user;

import com.example.Blog_App.authentication.dtos.SignUpDto;
import com.example.Blog_App.authentication.resend.MailService;
import com.example.Blog_App.exceptions.ResourceExistsException;
import com.example.Blog_App.exceptions.ResourceNotFoundException;
import com.example.Blog_App.user.dtos.UpdateUserDto;
import com.example.Blog_App.user.dtos.UserDto;
import com.resend.core.exception.ResendException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MailService mailService;

    public UserService(UserRepository userRepository, UserMapper userMapper, MailService mailService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.mailService = mailService;
    }

    private UserEntity findUser(UUID userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(false, "User not found with ID: " + userId));

        return user;
    }

    public String generateCode() {
        Random rnd = new Random();
        int code = rnd.nextInt(999999);
        return String.valueOf(code);
    }

    @Transactional
    public UserDto createUser(SignUpDto signUpDto, String encodedPassword) {
        String email = signUpDto.email();
        String name = signUpDto.name();

        if (userRepository.existsByEmail(email)) {
            throw new ResourceExistsException(false, "User already exists with email: " + email);
        }

        String code = generateCode();

        try {
            mailService.sendEmail(email, String.valueOf(code));
        } catch (ResendException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        UserEntity user = UserEntity.builder()
                .email(email)
                .name(name)
                .password(encodedPassword)
                .verificationCode(String.valueOf(code))
                .verificationCodeExpiresAt(new Date().toInstant().plus(Duration.ofMinutes(10)))
                .build();

        UserEntity savedUser = userRepository.save(user);
        
        return userMapper.toDto(savedUser);

    }

    @Transactional
    public UserDto updateUser(UpdateUserDto updateUserDto, UUID userId) {
        UserEntity user = findUser(userId);
        user.setEmail(updateUserDto.email());
        user.setName(updateUserDto.name());
        user.setUpdatedAt(new Date().toInstant());

        String biography = updateUserDto.biography();

        if (biography != null && !biography.isBlank()) user.setBiography(biography);

        UserEntity updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(UUID userId) {
        UserEntity user = findUser(userId);
        return userMapper.toDto(user);
    }

    @Transactional
    public void deleteUserById(UUID userId) {
        UserEntity user = findUser(userId);
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        // Retrieves all users from a database, converts them into DTOs, and returns them as a list.
        return userRepository.findAll().stream().map(user -> userMapper.toDto(user)).toList();
    }
}
