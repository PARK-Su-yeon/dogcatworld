package com.techeer.abandoneddog.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		Info info = new Info()
			.title("Pet API")
			.description("API for Pet Service");

		String apiKeySchemeName = "ApiKey Authentication";

		// 요청 헤더에 인증 정보 포함
		SecurityRequirement securityItem = new SecurityRequirement().addList(apiKeySchemeName);

		Components components = new Components()
			.addSecuritySchemes(apiKeySchemeName, new io.swagger.v3.oas.models.security.SecurityScheme()
				.name(apiKeySchemeName)
				.type(SecurityScheme.Type.APIKEY)
				.in(SecurityScheme.In.HEADER)
				.name("access"));

		return new OpenAPI()
			.info(info)
			.addSecurityItem(securityItem)
			.components(components);
	}
}