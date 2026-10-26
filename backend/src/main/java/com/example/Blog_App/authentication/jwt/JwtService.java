package com.example.Blog_App.authentication.jwt;

import com.example.Blog_App.user.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
@Getter
@Setter
public class JwtService {

    private final SecretKey key;
    private final long accessTokenTTLSeconds;
    private final long refreshTokenTTLSeconds;
    private final String issuer;

    public JwtService(@Value("${security.jwt.secret-key}") String secret,
                      @Value("${security.jwt.access-ttl-seconds}") long accessTokenTTLSeconds,
                      @Value("${security.jwt.refresh-ttl-seconds}") long refreshTokenTTLSeconds,
                      @Value("${security.jwt.issuer}") String issuer) {

        if (secret == null || secret.length() < 64) {
            throw new IllegalArgumentException("Invalid secret key");
        }

        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenTTLSeconds = accessTokenTTLSeconds;
        this.refreshTokenTTLSeconds = refreshTokenTTLSeconds;
        this.issuer = issuer;
    }

    public String generateAccessToken(UserEntity user) {
        Instant now = Instant.now();
        return Jwts.builder().id(UUID.randomUUID().toString()).subject(user.getId().toString()).issuer(issuer).issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(accessTokenTTLSeconds))).claims(Map.of("email", user.getEmail(), "type", "access")).signWith(key, Jwts.SIG.HS512).compact();
    }

    public String generateRefreshToken(UserEntity user, String jti) {
        Instant now = Instant.now();
        return Jwts.builder().id(jti).subject(user.getId().toString()).issuer(issuer).issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(refreshTokenTTLSeconds))).claim("type", "refresh").signWith(key, Jwts.SIG.HS512).compact();
    }

    // parse JWT
    public Jws<Claims> parse(String jwt) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt);
    }

    public boolean isAccessToken(String token) {
        Claims c = parse(token).getPayload();
        return "access".equals(c.get("type"));
    }

    public UUID getUserId(String token) {
        Claims c = parse(token).getPayload();
        return UUID.fromString(c.getSubject());
    }

    public String getJti(String token) {
        return parse(token).getPayload().getId();
    }

    public String getEmail(String token) {
        Claims c = parse(token).getPayload();
        return c.get("email", String.class);
    }
}