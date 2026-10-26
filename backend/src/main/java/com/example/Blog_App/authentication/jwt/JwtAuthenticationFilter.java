package com.example.Blog_App.authentication.jwt;

import com.example.Blog_App.user.UserRepository;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, java.io.IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                Jws<Claims> parse = jwtService.parse(token);
                Claims payload = parse.getPayload();

                if (!"access".equals(payload.get("type"))) {
                    filterChain.doFilter(request, response);
                    return;
                }

                String userId = payload.getSubject();
                UUID userUUID = UUID.fromString(userId);

                userRepository.findById(userUUID).ifPresent(user -> {
                    if (user.isEnabled()) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, user.getAuthorities());

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        if (SecurityContextHolder.getContext().getAuthentication() == null)
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                });

            } catch (ExpiredJwtException e) {
                request.setAttribute("error", "Token expired");
            } catch (JwtException | IllegalArgumentException e) {
                request.setAttribute("error", "Invalid token");
            } catch (Exception e) {
                request.setAttribute("error", "Internal server error");
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().startsWith("/api/auth");
    }
}
