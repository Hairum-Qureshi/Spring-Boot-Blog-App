package com.example.Blog_App.user;

import com.example.Blog_App.authentication.dtos.SignUpDto;
import com.example.Blog_App.exceptions.ResourceExistsException;
import com.example.Blog_App.exceptions.ResourceNotFoundException;
import com.example.Blog_App.user.dtos.UpdateUserDto;
import com.example.Blog_App.user.dtos.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    private UserEntity findUser(UUID userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(false, "User not found with ID: " + userId));

        return user;
    }

    @Transactional
    public UserDto createUser(SignUpDto signUpDto) {
        String email = signUpDto.email();
        String name = signUpDto.name();
        String password = signUpDto.password();

        if (userRepository.existsByEmail(email)) {
            throw new ResourceExistsException(false, "User already exists with email: " + email);
        }

        UserEntity user = UserEntity.builder()
                .email(email)
                .name(name)
                .password(password)
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
