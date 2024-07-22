package com.techeer.abandoneddog.users.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeer.abandoneddog.users.service.RedisService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomLogoutFilter extends GenericFilterBean {

	private final JWTUtil jwtUtil;
	private final RedisService redisService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public CustomLogoutFilter(JWTUtil jwtUtil, RedisService redisService) {

		this.jwtUtil = jwtUtil;
		this.redisService = redisService;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {

		doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
	}

	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
		IOException,
		ServletException {

		//path and method verify
		String requestUri = request.getRequestURI();
		if (!requestUri.matches("^\\/api/v1/users/logout$")) {

			filterChain.doFilter(request, response);
			return;
		}
		String requestMethod = request.getMethod();
		if (!requestMethod.equals("POST")) {

			filterChain.doFilter(request, response);
			return;
		}

		//get refresh token
		String refresh = extractCookieValue(request, "refresh");

		Map<String, Object> responseMap = new HashMap<>();
		response.setContentType("application/json");

		//refresh null 확인, expired 확인, 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
		if (refresh == null || !isValidRefreshToken(refresh)) {
			sendErrorResponse(response, "토큰이 유효하지 않습니다.");
			return;
		}

		String email = jwtUtil.getEmail(refresh);

		//Redis에 저장되어 있는지 확인
		Boolean isExist = redisService.hasKey(email);
		if (!isExist) {
			sendErrorResponse(response, "해당 토큰이 저장되어 있지 않습니다.");
			return;
		}

		//로그아웃 진행
		redisService.deleteValues(email);   //Refresh 토큰 Redis 제거
		deleteRefreshCookie(response);      //Refresh 토큰 쿠키 제거

		responseMap.put("message", "로그아웃 성공");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getOutputStream().write(objectMapper.writeValueAsBytes(responseMap));
	}

	private String extractCookieValue(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	private boolean isValidRefreshToken(String token) {
		try {
			return jwtUtil.isExpired(token) || "refresh".equals(jwtUtil.getCategory(token));
		} catch (ExpiredJwtException e) {
			return false;
		}
	}

	private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("error", errorMessage);
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.getOutputStream().write(objectMapper.writeValueAsBytes(responseMap));
	}

	private void deleteRefreshCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie("refresh", null);
		cookie.setMaxAge(0);
		cookie.setPath("/api/v1/users");
		response.addCookie(cookie);
	}
}