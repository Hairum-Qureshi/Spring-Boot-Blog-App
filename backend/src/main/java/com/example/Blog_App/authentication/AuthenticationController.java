package com.example.Blog_App.authentication;

import com.example.Blog_App.authentication.dtos.SignInDto;
import com.example.Blog_App.authentication.dtos.SignUpDto;
import com.example.Blog_App.authentication.refreshToken.RefreshTokenRequest;
import com.example.Blog_App.authentication.responses.TokenResponse;
import com.example.Blog_App.user.dtos.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
    public ResponseEntity<TokenResponse> signInUser(@RequestBody SignInDto signInDto, HttpServletResponse response) {
        TokenResponse tokenResponse = authService.signIn(signInDto, response);

        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verifyUser(@RequestBody Map<String, String> verificationCode) {
        String code = verificationCode.get("verificationCode");
        authService.verifyUser(code);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resend-verification-code")
    public ResponseEntity<Void> resendVerificationCode(@RequestBody Map<String, String> email) {
        authService.resendVerificationCode(email.get("email"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody(required = false) RefreshTokenRequest body, HttpServletRequest request, HttpServletResponse response) {
        TokenResponse tokenResponse = authService.refresh(body, request, response);
        return ResponseEntity.ok(tokenResponse);
    }
}
