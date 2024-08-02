package me.photo.photoapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info().title("Spring Photo API")
                        .description("스프링 사진 업로드 기능 테스트 API")
                        .version("v0.0.1"));
    }

}
