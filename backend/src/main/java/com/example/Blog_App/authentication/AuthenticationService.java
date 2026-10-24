package com.example.Blog_App.authentication;

import com.example.Blog_App.authentication.dtos.SignUpDto;
import com.example.Blog_App.exceptions.PasswordsNotMatchingException;
import com.example.Blog_App.user.UserService;
import com.example.Blog_App.user.dtos.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    public AuthenticationService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto signUpUser(SignUpDto signUpDto) {
        String password = signUpDto.password();
        String confirmPassword = signUpDto.confirmedPassword();

        if (!password.equals(confirmPassword)) {
            throw new PasswordsNotMatchingException("Passwords do not match");
        }

        String encodedPassword = passwordEncoder.encode(password);

        return userService.createUser(signUpDto, encodedPassword);
    }

//    public UserDto signInUser(SignInDto signInDto) {
//
//    }
}
