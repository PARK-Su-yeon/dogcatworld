package com.techeer.abandoneddog.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.CorsFilter;

import com.techeer.abandoneddog.users.jwt.CustomLogoutFilter;
import com.techeer.abandoneddog.users.jwt.JWTFilter;
import com.techeer.abandoneddog.users.jwt.JWTUtil;
import com.techeer.abandoneddog.users.jwt.LoginFilter;
import com.techeer.abandoneddog.users.repository.RefreshRepository;
import com.techeer.abandoneddog.users.service.RedisService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	//AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
	private final AuthenticationConfiguration authenticationConfiguration;
	private final JWTUtil jwtUtil;
	private final RefreshRepository refreshRepository;
	private final CorsFilter corsFilter;

	public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil,
		RefreshRepository refreshRepository, CorsFilter corsFilter) {
		this.authenticationConfiguration = authenticationConfiguration;
		this.jwtUtil = jwtUtil;
		this.refreshRepository = refreshRepository;
		this.corsFilter = corsFilter;
	}

	//AuthenticationManager Bean 등록
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

		return configuration.getAuthenticationManager();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, RedisService redisService) throws Exception {

		http
			.csrf((auth) -> auth.disable());

		http
			.formLogin((auth) -> auth.disable());

		http
			.httpBasic((auth) -> auth.disable());

    http
        .authorizeHttpRequests((auth) -> auth
            .requestMatchers("/api/v1/users/login", "/api/v1/users/register", "/login", "/reissue",
                "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/api/v1/pet-funeral",
                "/api/v1/shelter/*", "/api/v1/shelter_address",  "/api/v1/pet_board/**").permitAll()
            .anyRequest().authenticated());
    // .anyRequest().permitAll());

		// CorsFilter 추가
		http.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class);

		//JWTFilter 등록
		http
			.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

		http
			.addFilterBefore(new CustomLogoutFilter(jwtUtil, redisService), LogoutFilter.class);
		//필터 추가 LoginFilter()는 인자를 받음 (AuthenticationManager() 메소드에 authenticationConfiguration 객체를 넣어야 함) 따라서 등록 필요
		http
			.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, redisService),
				UsernamePasswordAuthenticationFilter.class);

		http
			.sessionManagement((session) -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}