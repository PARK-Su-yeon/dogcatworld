//package com.techeer.abandoneddog.global.config;
//
//import io.swagger.v3.oas.models.Components;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.context.SecurityContext;
//
//import java.util.List;
//
//@Configuration    // 스프링 실행시 설정파일 읽어드리기 위한 어노테이션
//public class SwaggerConfig {
//
//    @Bean
//    public OpenAPI openAPI() {
//        return new OpenAPI()
//                .components(new Components())
//                .info(apiInfo());
//    }
//
//    private Info apiInfo() {
//        return new Info()
//                .title("CodeArena Swagger")
//                .description("CodeArena 유저 및 인증 , ps, 알림에 관한 REST API")
//                .version("1.0.0")
//                .securityContexts(List.of(this.securityContext())) // SecurityContext 설정
//                .securitySchemes(List.of(this.apiKey())) // ApiKey 설정
//    }
//    private SecurityContext securityContext() {
//        return SecurityContext.builder()
//                .securityReferences(defaultAuth())
//                .build();
//    }
//
//    private List<SecurityReference> defaultAuth() {
//        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        return List.of(new SecurityReference("Authorization", authorizationScopes));
//    }
//
//}
