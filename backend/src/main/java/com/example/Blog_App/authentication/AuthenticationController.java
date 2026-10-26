package com.example.Blog_App.authentication;

import com.example.Blog_App.authentication.dtos.SignInDto;
import com.example.Blog_App.authentication.dtos.SignUpDto;
import com.example.Blog_App.authentication.jwt.JwtService;
import com.example.Blog_App.authentication.responses.TokenResponse;
import com.example.Blog_App.user.UserEntity;
import com.example.Blog_App.user.UserMapper;
import com.example.Blog_App.user.UserRepository;
import com.example.Blog_App.user.dtos.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthenticationController(AuthenticationService authService, AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService, UserMapper userMapper) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> signUpUser(@RequestBody SignUpDto signUpDto) {
        return ResponseEntity.ok(authService.signUpUser(signUpDto));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<TokenResponse> signInUser(@RequestBody SignInDto signInDto) {
        authenticate(signInDto);

        UserEntity user = userRepository.findByEmail(signInDto.email()).orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!user.isEnabled()) throw new DisabledException("User account is disabled");

        String accessToken = jwtService.generateAccessToken(user);

        TokenResponse tokenResponse = TokenResponse.of(accessToken, "", jwtService.getAccessTokenTTLSeconds(), userMapper.toDto(user));

        return ResponseEntity.ok(tokenResponse);
    }

    private Authentication authenticate(SignInDto signInDto) {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDto.email(), signInDto.password()));
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
