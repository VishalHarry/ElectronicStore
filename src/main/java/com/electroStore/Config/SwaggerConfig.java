package com.electroStore.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        final String securitySchemeName = "JWT Token";

        return new OpenAPI()
                .info(new Info()
                        .title("ElectroStore API Documentation")
                        .version("1.0")
                        .description("This API provides endpoints for the ElectroStore Backend System")
                        .contact(new Contact()
                                .name("Vishal Tomar")
                                .email("vreducation88@gmail.com")
                                .url("https://v0-react-navbar-design-eight.vercel.app/")
                        )
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")
                        )
                )
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Server"),
                        new Server().url("https://electrostore-api.vercel.app").description("Production Server")
                ))

                // 🔐 Add JWT requirement to Swagger UI
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))

                // 🔐 Add JWT Authentication scheme
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .description("Enter JWT token in format: Bearer <token>")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}
