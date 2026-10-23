package com.example.Blog_App.user;

import com.example.Blog_App.exceptions.ResourceNotFoundException;
import com.example.Blog_App.user.enums.Provider;
import org.springframework.stereotype.Service;

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

    public UserDto createUser(UserDto userDto) {

        String email = userDto.email();
        String name = userDto.name();
        boolean enabled = userDto.enabled();
        String profilePicture = userDto.profilePicture();
        String biography = userDto.biography();

        Provider provider = userDto.provider() != null ? userDto.provider() : Provider.LOCAL;

        UserEntity user = UserEntity.builder()
                .email(email)
                .name(name)
                .enabled(enabled)
                .profilePicture(profilePicture)
                .biography(biography)
                .provider(provider)
                .build();

        UserEntity savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    public UserDto getUserByEmail(String email) {

        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(false, "User not found with email: " + email));

        UserDto userDto = userMapper.toDto(user);

        return userDto;
    }

    public UserDto updateUser(String email, UUID userId) {
        UserEntity user = findUser(userId);
        user.setEmail(email);
        UserEntity updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    public UserDto getUserById(UUID userId) {
        UserEntity user = findUser(userId);
        return userMapper.toDto(user);
    }

    public void deleteUserById(UUID userId) {
        UserEntity user = findUser(userId);
        userRepository.delete(user);
    }

    public List<UserDto> getAllUsers() {
        // Retrieves all users from a database, converts them into DTOs, and returns them as a list.
        return userRepository.findAll().stream().map(user -> userMapper.toDto(user)).toList();
    }
}
