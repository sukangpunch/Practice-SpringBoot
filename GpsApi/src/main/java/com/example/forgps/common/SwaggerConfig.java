package com.example.forgps.common;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("gps 적용시키기")
                        .description("캡스톤 gps 적용을 위함")
                        .version("0.0.1-SNAPSHOT"));
    }
}
