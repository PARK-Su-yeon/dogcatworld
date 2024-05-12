package com.techeer.abandoneddog.users.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeer.abandoneddog.users.service.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private static final String DEFAULT_LOGIN_REQUEST_URL = "/api/v1/users/login";
    private static final String HTTP_METHOD_POST = "POST";
    private static final String CONTENT_TYPE = "application/json"; // JSON
    private static final String USERNAME_KEY = "email";
    private static final String PASSWORD_KEY = "password";

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RedisService redisService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RedisService redisService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD_POST));

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if (!request.getContentType().equals(CONTENT_TYPE)) {
            throw new AuthenticationServiceException("Unsupported content type: " + request.getContentType());
        }

        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

        //클라이언트 요청에서 username, password 추출
        //JSON 문자열을 Map으로 변환
        Map<String, String> credentials = objectMapper.readValue(sb.toString(), Map.class);

        //JSON 키로부터 username과 password 추출
        String email = credentials.get(USERNAME_KEY);
        String password = credentials.get(PASSWORD_KEY);

        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Failed to parse authentication request body", e);
        }
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        //유저 정보
        String username = authentication.getName();

        //토큰 생성
        String access = jwtUtil.createJwt("access", username, 600000L);
        String refresh = jwtUtil.createJwt("refresh", username, 86400000L);

        //Refresh 토큰 저장
        addRefreshRedis(username, refresh);

        //응답 설정 쿠키: refresh, 헤더: access
        response.addCookie(createCookie("refresh", refresh));
        response.setHeader("access", access);

        // JSON 응답 데이터 구성
        Map<String, String> tokens = new HashMap<>();
        tokens.put("username", username);
        tokens.put("access_token", access);
        tokens.put("refresh_token", refresh);


        // 응답 설정 JSON
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK.value());
        response.getOutputStream().write(objectMapper.writeValueAsBytes(tokens));
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

        Map<String, Object> responseData = new HashMap<>();
        response.setContentType("application/json;charset=UTF-8");

        if (failed instanceof UsernameNotFoundException) {
            responseData.put("error", "User not found");
            response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401 status code
        } else if (failed instanceof BadCredentialsException) {
            responseData.put("error", "Invalid password");
            response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401 status code
        } else {
            responseData.put("error", "Authentication failed");
            response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401 status code
        }

        response.getOutputStream().write(objectMapper.writeValueAsBytes(responseData));
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

