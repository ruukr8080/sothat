package com.ex.sothat.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
            .info(new Info()
            .title("soThat 프로젝트 API")
            .description("협업툴입니다.")
            .version("1.0.0"));
    }
}
