package com.techeer.abandoneddog.users.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface ReissueService {
    String extractTokenFromCookies(HttpServletRequest request, String tokenName);

    void NewToken(String email, HttpServletResponse response);

    Optional<String> reissueTokens(HttpServletRequest request, HttpServletResponse response);
}
