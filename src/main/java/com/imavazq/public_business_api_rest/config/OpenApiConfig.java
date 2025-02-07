package com.imavazq.public_business_api_rest.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                    name = "Imanol Vázquez",
                        email = "vazquezimanol1@gmail.com"
                ),
                description = "OpenApi documentation for Simple Business API REST",
                title = "OpenApi specification - imavazq",
                version = "1.0",
                license = @License(
                        name = "License name"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                ),
                @Server(
                    description = "Production ENV",
                    url = "http://simplebusinessapirest.com/"
                )
        }
)
public class OpenApiConfig {
}
