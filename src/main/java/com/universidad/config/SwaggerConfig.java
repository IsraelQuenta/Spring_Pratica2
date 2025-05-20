package com.universidad.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI universidadOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Universidad")
                        .version("1.0")
                        .description("Documentación de la API para gestión de materias, docentes e inscripciones."));
    }
}