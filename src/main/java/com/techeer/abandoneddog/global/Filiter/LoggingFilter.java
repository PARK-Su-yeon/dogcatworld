package com.techeer.abandoneddog.global.Filiter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

public class LoggingFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest)request;

		// 요청의 Content-Type 로깅
		log.info("Request Content-Type: {}", httpRequest.getContentType());

		// 각 파트의 Content-Type 로깅
		if (httpRequest.getContentType() != null && httpRequest.getContentType().startsWith("multipart/form-data")) {
			for (Part part : httpRequest.getParts()) {
				log.info("Part Name: {}", part.getName());
				log.info("Part Content-Type: {}", part.getContentType());
			}
		}

		// 필터 체인 계속 진행
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// 소멸 코드 (필요한 경우)
	}
}
