package com.techeer.abandoneddog.users.controller;

import com.techeer.abandoneddog.users.jwt.JWTUtil;
import com.techeer.abandoneddog.users.service.RedisService;
import com.techeer.abandoneddog.users.service.ReissueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@ResponseBody
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final RedisService redisService;
    private final ReissueService reissueService;

    public ReissueController(JWTUtil jwtUtil, RedisService redisService, ReissueService reissueService) {

        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
        this.reissueService = reissueService;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        Optional<String> error = reissueService.reissueTokens(request, response);

        if (error.isPresent()) {
            return new ResponseEntity<>(error.get(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}