package com.esquad.esquadbe.global.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI(@Value("${springdoc.api-docs.version}") String springdocVersion) {
        Info info = new Info()
            .title("제목")
            .version(springdocVersion)
            .description("설명");

        return new OpenAPI()
            .components(new Components())
            .info(info);
    }
}
