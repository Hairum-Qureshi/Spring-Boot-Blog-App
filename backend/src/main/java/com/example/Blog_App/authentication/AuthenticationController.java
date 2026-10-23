package com.example.Blog_App.authentication;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

//    ResponseEntity<SignUpDto> signUpUser(@RequestBody SignUpDto signUpDto) {
//    }
//
//    ResponseEntity<Void> signInUser(@RequestBody SignInDto signInDto) {
//    }
}
