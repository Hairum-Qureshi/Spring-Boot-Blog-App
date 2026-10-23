package com.example.Blog_App.authentication;

import com.example.Blog_App.authentication.dtos.SignUpDto;
import com.example.Blog_App.exceptions.PasswordsNotMatchingException;
import com.example.Blog_App.user.UserService;
import com.example.Blog_App.user.dtos.UserDto;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private UserService userService;

    public AuthenticationService(UserService userService) {
        this.userService = userService;
    }

    public UserDto signUpUser(SignUpDto signUpDto) {
        String password = signUpDto.password();
        String confirmPassword = signUpDto.confirmedPassword();

        if (!password.equals(confirmPassword)) {
            throw new PasswordsNotMatchingException("Passwords do not match");
        }

        UserDto userDto = userService.createUser(signUpDto);
        return userDto;
    }
}
