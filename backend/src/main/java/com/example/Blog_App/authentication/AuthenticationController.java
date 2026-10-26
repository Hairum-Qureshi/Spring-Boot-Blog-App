package com.example.Blog_App.authentication;

import com.example.Blog_App.authentication.dtos.SignInDto;
import com.example.Blog_App.authentication.dtos.SignUpDto;
import com.example.Blog_App.authentication.responses.TokenResponse;
import com.example.Blog_App.user.dtos.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;

    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> signUpUser(@RequestBody SignUpDto signUpDto) {
        return ResponseEntity.ok(authService.signUpUser(signUpDto));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<TokenResponse> signInUser(@RequestBody SignInDto signInDto) {
        TokenResponse tokenResponse = authService.signin(signInDto);

        return ResponseEntity.ok(tokenResponse);
    }
}
