package com.daniellecaricati.personalblog.configuration;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.security.authentication.AuthenticationManager;

@Configuration
public class SwaggerConfig {
	
	private final AuthenticationManager authenticationManager;

	SwaggerConfig(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Bean
	OpenAPI springPersonalBlogOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Personal Blog Project")
						.description("Personal Blog Project - Danielle Caricati")
						.version("v0.0.1")
						.license(new License()
								.name("Danielle Caricati")
								.url("https://daniellecaricati.com/"))
						.contact(new Contact()
								.name("Danielle Caricati")
								.url("https://github.com/daniellecaricati/")
								.email("daniellecaricati@gmail.com")))
				.externalDocs(new ExternalDocumentation()
						.description("GitHub")
						.url("https://github.com/daniellecaricati/personal_blog_backend/"))
				.components(new Components().addSecuritySchemes("jwt_auth", createSecurityScheme()))
					.addSecurityItem(new SecurityRequirement().addList("jwt_auth"));
	}
	
	@Bean
	OpenApiCustomizer customerGlobalHeaderOpenApiCustomiser() {
		return openApi -> {
			openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
				ApiResponses apiResponses = operation.getResponses();			
				
				apiResponses.addApiResponse("200", createApiResponse("Sucess!"));
				apiResponses.addApiResponse("201", createApiResponse("Object Persisted!"));
				apiResponses.addApiResponse("204", createApiResponse("Object Deleted!"));
				apiResponses.addApiResponse("400", createApiResponse("Bad Request!"));
				apiResponses.addApiResponse("401", createApiResponse("Unauthorized Access!"));
				apiResponses.addApiResponse("403", createApiResponse("Forbidden!"));
				apiResponses.addApiResponse("404", createApiResponse("Object Not Found!"));
				apiResponses.addApiResponse("500", createApiResponse("Internal Server Error!"));						
			}));
		};
	}
	
	private ApiResponse createApiResponse(String message) {
		return new ApiResponse().description(message);
	}
	
	private SecurityScheme createSecurityScheme() {
		return new SecurityScheme()
				.name("jwt_auth")
				.type(SecurityScheme.Type.HTTP)
				.scheme("bearer")
				.bearerFormat("JWT")
				.description("Enter the JWT token only, (the word Bearer will be added automatically)");
	}
}
