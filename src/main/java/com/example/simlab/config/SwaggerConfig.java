package com.example.simlab.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Swagger/OpenAPI para documentação da API.
 *
 * <p>Define informações gerais sobre a API que aparecem na documentação interativa.</p>
 *
 * @author Amanda
 * @version 1.0
 * @since 2026-01-22
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configura as informações da API exibidas no Swagger UI.
     *
     * @return Objeto OpenAPI com metadados da API
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SimLab API")
                        .version("1.0")
                        .description("API REST para gerenciamento de laboratório médico - Sistema de pacientes e exames")
                        .contact(new Contact()
                                .name("Amanda Sofia")
                                .email("nana.sofia.it@gmail.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
