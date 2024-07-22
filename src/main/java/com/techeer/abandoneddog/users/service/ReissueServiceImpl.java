package com.techeer.abandoneddog.users.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.techeer.abandoneddog.users.jwt.JWTUtil;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReissueServiceImpl implements ReissueService {

	private final JWTUtil jwtUtil;
	private final RedisService redisService;

	@Override
	public String extractTokenFromCookies(HttpServletRequest request, String tokenName) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			return null;
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
	public Map<String, String> reissueTokens(HttpServletRequest request, HttpServletResponse response) {

		Map<String, String> result = new HashMap<>();

		String refresh = extractTokenFromCookies(request, "refresh");

		if (refresh == null) {
			result.put("error", "Refresh token is missing");
			return result;
		}

		try {
			if (jwtUtil.isExpired(refresh)) {
				result.put("error", "Refresh token expired");
				return result;
			}

			if (!"refresh".equals(jwtUtil.getCategory(refresh))) {
				result.put("error", "Invalid refresh token");
				return result;
			}

			String email = jwtUtil.getEmail(refresh);

			if (!redisService.hasKey(email)) {
				result.put("error", "No refresh token stored");
				return result;
			}
			// 새 토큰 생성 및 응답 처리
			NewToken(email, response);
			result.put("access", jwtUtil.createJwt("access", email, 600000L)); // 10분 유효
			result.put("refresh", jwtUtil.createJwt("refresh", email, 86400000L));  // 24시간 유효

		} catch (ExpiredJwtException e) {
			result.put("error", "Refresh token expired");
			return result;
		}

		return result;
	}

	private Cookie createCookie(String key, String value) {

		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(24 * 60 * 60);
		cookie.setPath("/api/v1/users"); // 쿠키 경로 설정
		cookie.setHttpOnly(true);

		return cookie;
	}

	// Redis에 저장
	private void addRefreshRedis(String username, String refresh) {

		redisService.setValues(username, refresh);

	}
}
