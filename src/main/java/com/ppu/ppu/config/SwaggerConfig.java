package com.ppu.ppu.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("PPU API")
                .version("0.0.1")
                .description("PPU API 문서입니다.");
        SecurityRequirement sr = new SecurityRequirement().addList("Auth");
        Components components = new Components()
                .addSecuritySchemes("Auth", new SecurityScheme()
                        .name("Auth")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("Bearer")
                        .bearerFormat("JWT"));
        return new OpenAPI()
                .info(info)
                .addSecurityItem(sr)
                .components(components);
    }
}