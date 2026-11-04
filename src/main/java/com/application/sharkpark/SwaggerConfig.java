package com.application.sharkpark;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title("Shark park API")
                                            .description(
                                                    "Order management API for the Shark park project")
                                            .version("1.0.0"));
//                            .components(new Components().addSecuritySchemes("Authorization",
//                                                                            new SecurityScheme().type(
//                                                                                                        SecurityScheme.Type.APIKEY)
//                                                                                                .in(SecurityScheme.In.HEADER)
//                                                                                                .name("Authorization")));
    }
}