package com.example.Blog_App.authentication.cookies;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class CookieService {
    private final String refreshTokenCookieName;
    private final boolean httpOnlyCookie;
    private final boolean secureCookie;
    private final String cookieDomain;
    private final String cookieSameSite;

    public CookieService(@Value("${security.jwt.refresh-token-cookie-name}") String refreshTokenCookieName,
                         @Value("${security.jwt.cookie-http-only}") boolean httpOnlyCookie,
                         @Value("${security.jwt.secure}") boolean secureCookie,
                         @Value("${security.jwt.cookie-domain}") String cookieDomain,
                         @Value("${security.jwt.cookie-same-site}") String cookieSameSite) {
        this.refreshTokenCookieName = refreshTokenCookieName;
        this.httpOnlyCookie = httpOnlyCookie;
        this.secureCookie = secureCookie;
        this.cookieDomain = cookieDomain;
        this.cookieSameSite = cookieSameSite;
    }

    public void attachRefreshCookie(HttpServletResponse response, String value, int maxAge) {
        var responseCookieBuilder = ResponseCookie.from(refreshTokenCookieName, value)
                .httpOnly(httpOnlyCookie)
                .secure(secureCookie)
                .domain(cookieDomain)
                .path("/")
                .maxAge(maxAge)
                .sameSite(cookieSameSite).domain(cookieDomain)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, responseCookieBuilder.toString());
    }

    public void clearRefreshCookie(HttpServletResponse response) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(refreshTokenCookieName, "").maxAge(0).httpOnly(httpOnlyCookie).path("/").sameSite(cookieSameSite).domain(cookieDomain);

        response.addHeader(HttpHeaders.SET_COOKIE, builder.build().toString());
    }

    public void addNoStoreHeaders(HttpServletResponse response) {
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store");
        response.setHeader("Pragma", "no-cache");
    }
}
