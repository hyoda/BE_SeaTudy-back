package com.finalproject.seatudy.service;

import com.finalproject.seatudy.security.UserDetailsImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final String API_NAME = "SeaTudy REST API";
    private static final String API_VERSION = "1.0.0";
    private static final String API_DESCRIPTION = "SeaTudy 서버 API 문서";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(UserDetailsImpl.class)
                .apiInfo(apiInfo())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/v1/**"))
                .build();
    }

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                .title(API_NAME)
                .version(API_VERSION)
                .description(API_DESCRIPTION)
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope =
                new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes =
                new AuthorizationScope[1];

        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(
                new SecurityReference("JWT", authorizationScopes));
    }
}
