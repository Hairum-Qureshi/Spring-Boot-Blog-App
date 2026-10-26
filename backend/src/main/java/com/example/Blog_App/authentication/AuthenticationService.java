package com.example.Blog_App.authentication;

import com.example.Blog_App.authentication.cookies.CookieService;
import com.example.Blog_App.authentication.dtos.SignInDto;
import com.example.Blog_App.authentication.dtos.SignUpDto;
import com.example.Blog_App.authentication.jwt.JwtService;
import com.example.Blog_App.authentication.refreshToken.RefreshTokenEntity;
import com.example.Blog_App.authentication.refreshToken.RefreshTokenRepository;
import com.example.Blog_App.authentication.resend.MailService;
import com.example.Blog_App.authentication.responses.TokenResponse;
import com.example.Blog_App.exceptions.InvalidVerificationCodeException;
import com.example.Blog_App.exceptions.PasswordsNotMatchingException;
import com.example.Blog_App.exceptions.ResourceNotFoundException;
import com.example.Blog_App.user.UserEntity;
import com.example.Blog_App.user.UserMapper;
import com.example.Blog_App.user.UserRepository;
import com.example.Blog_App.user.UserService;
import com.example.Blog_App.user.dtos.UserDto;
import com.resend.core.exception.ResendException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthenticationService {

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MailService mailService;
    private final CookieService cookieService;

    public AuthenticationService(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService, UserMapper userMapper, RefreshTokenRepository refreshTokenRepository, MailService mailService, CookieService cookieService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.cookieService = cookieService;
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

    public void verifyUser(String code) {

        if (code == null || code.isBlank())
            throw new ResourceNotFoundException(false, "Verification code is required");

        UserEntity user = userRepository.findByVerificationCode(code).orElseThrow(() -> new InvalidVerificationCodeException("Invalid verification code: " + code));

        if (user.getVerificationCodeExpiresAt().isBefore(Instant.now())) {
            throw new InvalidVerificationCodeException("Verification code has expired. Please request a new one.");
        }

        user.setVerificationCode(null);
        user.setVerified(true);
        user.setVerificationCodeExpiresAt(null);

        userRepository.save(user);
    }

    public void resendVerificationCode(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(false, "User not found"));

        if (user.isVerified()) {
            throw new InvalidVerificationCodeException("User is already verified");
        }

        String verificationCode = userService.generateCode();
        user.setVerificationCode(verificationCode);
        user.setVerificationCodeExpiresAt(new Date().toInstant().plus(Duration.ofMinutes(10)));
        userRepository.save(user);

        try {
            mailService.sendEmail(email, verificationCode);
        } catch (ResendException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TokenResponse signIn(SignInDto signInDto, HttpServletResponse response) {
        UserEntity user = userRepository.findByEmail(signInDto.email()).orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!user.isVerified()) {
            throw new IllegalArgumentException("Your account is not verified. Please verify your email.");
        }

        authenticate(signInDto);

        if (!user.isEnabled()) throw new DisabledException("User account is disabled");

        String jti = UUID.randomUUID().toString();
        var buildRefreshToken = RefreshTokenEntity.builder().jti(jti).user(user).createdAt(Instant.now()).expiresAt(Instant.now().plusSeconds(jwtService.getAccessTokenTTLSeconds())).revoked(false).build();

        refreshTokenRepository.save(buildRefreshToken);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user, jti);

        cookieService.attachRefreshCookie(response, refreshToken, (int) jwtService.getRefreshTokenTTLSeconds());
        cookieService.addNoStoreHeaders(response);

        return TokenResponse.of(accessToken, refreshToken, jwtService.getAccessTokenTTLSeconds(), userMapper.toDto(user));
    }

    private Authentication authenticate(SignInDto signInDto) {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDto.email(), signInDto.password()));
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
