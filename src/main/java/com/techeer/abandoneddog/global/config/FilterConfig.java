package com.techeer.abandoneddog.global.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.techeer.abandoneddog.global.Filiter.LoggingFilter;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<LoggingFilter> loggingFilter() {
		FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new LoggingFilter());
		registrationBean.addUrlPatterns("/*"); // 모든 URL 패턴에 대해 필터 적용
		registrationBean.setOrder(1); // 필터 순서 설정 (낮을수록 먼저 실행)
		return registrationBean;
	}
}
