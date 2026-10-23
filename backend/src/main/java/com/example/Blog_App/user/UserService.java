package com.example.Blog_App.user;

import com.example.Blog_App.user.enums.Provider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDto createUser(UserDto userDto) {

        String email = userDto.email();
        String name = userDto.name();
        boolean enabled = userDto.enabled();
        String profilePicture = userDto.profilePicture();
        String biography = userDto.biography();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

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
        return null;
    }

    public UserDto updateUser(String email, String userId) {
        return null;
    }

    public void deleteUser(String email) {
        // Implement the logic to delete a user by email
    }

    public UserDto getUserById(String userId) {
        return null;
    }

    public List<UserDto> getAllUsers() {
        // Retrieves all users from a database, converts them into DTOs, and returns them as a list.
        return userRepository.findAll().stream().map(user -> userMapper.toDto(user)).toList();
    }
}
