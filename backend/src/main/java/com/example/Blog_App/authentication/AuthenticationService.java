package com.example.Blog_App.authentication;

import com.example.Blog_App.authentication.dtos.SignInDto;
import com.example.Blog_App.authentication.dtos.SignUpDto;
import com.example.Blog_App.authentication.jwt.JwtService;
import com.example.Blog_App.authentication.responses.TokenResponse;
import com.example.Blog_App.exceptions.PasswordsNotMatchingException;
import com.example.Blog_App.user.UserEntity;
import com.example.Blog_App.user.UserMapper;
import com.example.Blog_App.user.UserRepository;
import com.example.Blog_App.user.UserService;
import com.example.Blog_App.user.dtos.UserDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthenticationService(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService, UserMapper userMapper) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
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

    public TokenResponse signin(SignInDto signInDto) {
        authenticate(signInDto);

        UserEntity user = userRepository.findByEmail(signInDto.email()).orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!user.isEnabled()) throw new DisabledException("User account is disabled");

        String accessToken = jwtService.generateAccessToken(user);

        TokenResponse tokenResponse = TokenResponse.of(accessToken, "", jwtService.getAccessTokenTTLSeconds(), userMapper.toDto(user));
        return tokenResponse;
    }

    private Authentication authenticate(SignInDto signInDto) {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDto.email(), signInDto.password()));
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
