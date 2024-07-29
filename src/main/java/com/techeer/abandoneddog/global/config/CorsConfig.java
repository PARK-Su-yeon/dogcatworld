package com.techeer.abandoneddog.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

	@Value("${cors.allowed.origin1}")
	private String allowedOrigin1;

	@Value("${cors.allowed.origin2}")
	private String allowedOrigin2;

	@Value("${cors.allowed.origin3}")
	private String allowedOrigin3;

	@Value("${cors.allowed.origin4}")
	private String allowedOrigin4;

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin(allowedOrigin1);
		config.addAllowedOrigin(allowedOrigin2);
		config.addAllowedOrigin(allowedOrigin3);
		config.addAllowedOrigin(allowedOrigin4);
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}