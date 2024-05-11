package com.techeer.abandoneddog.users.service;

import com.techeer.abandoneddog.users.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReissueServiceImpl implements ReissueService {

    private final JWTUtil jwtUtil;
    private final RedisService redisService;

    @Override
    public String extractTokenFromCookies(HttpServletRequest request, String tokenName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(tokenName)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    public void NewToken(String email, HttpServletResponse response) {

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", email, 600000L); // 10분 유효
        String newRefresh = jwtUtil.createJwt("refresh", email, 86400000L);  // 24시간 유효

        //Refresh 토큰 Redis에 재저장
        redisService.deleteValues(email);
        addRefreshRedis(email, newRefresh);

        //response
        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));
    }

    @Override
    public Optional<String> reissueTokens(HttpServletRequest request, HttpServletResponse response) {
        String refresh = extractTokenFromCookies(request, "refresh");

        if (refresh == null) {
            return Optional.of("Refresh token is missing");
        }

        try {
            if (jwtUtil.isExpired(refresh)) {
                return Optional.of("Refresh token expired");
            }

            if (!"refresh".equals(jwtUtil.getCategory(refresh))) {
                return Optional.of("Invalid refresh token");
            }

            String email = jwtUtil.getEmail(refresh);

            if (!redisService.hasKey(email)) {
                return Optional.of("No refresh token stored");
            }

            NewToken(email, response);
        } catch (ExpiredJwtException e) {
            return Optional.of("Refresh token expired");
        }

        return Optional.empty();
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    // Redis에 저장
    private void addRefreshRedis(String username, String refresh) {

        redisService.setValues(username, refresh);

    }
}
