package com.example.Blog_App.authentication.oAuth2;

import com.example.Blog_App.authentication.cookies.CookieService;
import com.example.Blog_App.authentication.jwt.JwtService;
import com.example.Blog_App.authentication.refreshToken.RefreshTokenEntity;
import com.example.Blog_App.authentication.refreshToken.RefreshTokenRepository;
import com.example.Blog_App.user.UserEntity;
import com.example.Blog_App.user.UserRepository;
import com.example.Blog_App.user.enums.Provider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Component
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieService cookieService;

    public Oauth2SuccessHandler(UserRepository userRepository, JwtService jwtService, RefreshTokenRepository refreshTokenRepository, CookieService cookieService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.cookieService = cookieService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String registrationId = "unknown";

        if (authentication instanceof OAuth2AuthenticationToken token) {
            registrationId = token.getAuthorizedClientRegistrationId();
        }

        UserEntity user;
        switch (registrationId) {
            case "google":
                String googleId = oAuth2User.getAttributes().getOrDefault("sub", "").toString();
                String email = oAuth2User.getAttributes().getOrDefault("email", "").toString();
                String name = oAuth2User.getAttributes().getOrDefault("name", "").toString();
                String picture = oAuth2User.getAttributes().getOrDefault("picture", "").toString();
                UserEntity createdUser = UserEntity.builder().name(name).email(email).password("").profilePicture(picture).enabled(true).providerId(googleId).createdAt(Instant.now()).updatedAt(Instant.now()).provider(Provider.GOOGLE).isVerified(true).verificationCode("").verificationCodeExpiresAt(null).build();

                user = userRepository.findByEmail(email).orElseGet(() -> userRepository.save(createdUser));

                break;
            default:
                throw new RuntimeException("Invalid registration ID");
        }

        String jti = UUID.randomUUID().toString();
        RefreshTokenEntity refreshTokenBuilder = RefreshTokenEntity.builder().jti(jti).user(user).createdAt(Instant.now()).expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTokenTTLSeconds())).revoked(false).build();
        refreshTokenRepository.save(refreshTokenBuilder);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user, refreshTokenBuilder.getJti());

        cookieService.attachRefreshCookie(response, refreshToken, (int) jwtService.getRefreshTokenTTLSeconds());

        response.getWriter().write("Login Successful");
    }


}
