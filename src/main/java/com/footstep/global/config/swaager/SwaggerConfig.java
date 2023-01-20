package com.footstep.global.config.swaager;

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

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(accessApiKey(), refreshApiKey()))
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.footstep.domain"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("당신의 발자취 API")
                .version("1.0")
                .description("당신의 발자취의 API 명세서입니다.")
                .build();
    }

    private ApiKey accessApiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private ApiKey refreshApiKey() {
        return new ApiKey("RefreshToken", "RefreshToken", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope authorizationScope1 = new AuthorizationScope("global", "refreshEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        AuthorizationScope[] authorizationScopes1 = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        authorizationScopes1[0] = authorizationScope1;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes), new SecurityReference("RefreshToken", authorizationScopes1));
    }
}