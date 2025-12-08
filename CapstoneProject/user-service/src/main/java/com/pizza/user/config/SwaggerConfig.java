package com.pizza.user.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI userServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Service API")
                        .description("Pizza Ordering System - User Service")
                        .version("1.0"));
    }
}