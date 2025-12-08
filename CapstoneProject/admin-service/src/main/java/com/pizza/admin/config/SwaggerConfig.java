package com.pizza.admin.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI adminServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Admin Service API")
                        .description("Pizza Ordering System - Admin Service")
                        .version("1.0"));
    }
}