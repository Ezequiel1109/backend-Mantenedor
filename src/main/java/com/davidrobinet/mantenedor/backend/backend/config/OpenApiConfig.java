package com.davidrobinet.mantenedor.backend.backend.config;

import org.springframework.context.annotation.*;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API REST de Tareas")
                        .version("1.0.0")
                        .description("Documentación de la API para gestionar tareas"));
    }
}
