package br.com.optimus.web.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.function.Function;

@Primary
@Configuration
@OpenAPIDefinition(
        security = @SecurityRequirement(name = "bearer-jwt")
)
@SecurityScheme(
        name = "bearer-jwt",
        bearerFormat = "JWT",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer"
)
public class OpenApiConfig {

    @Value("${spring.application.name}")
    private String nameApp;
    @Value("${spring.application.description}")
    private String descriptionApp;

    private final static Function<String, ApiResponse> fnApiResponse = (info) -> new ApiResponse().description(info)
            .content(new Content().addMediaType(
                    org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                    new io.swagger.v3.oas.models.media.MediaType()
            ));

    @Bean
    public GroupedOpenApi consistencyUsersAPI() {
        return GroupedOpenApi.builder()
                .group(nameApp)
                .pathsToMatch("/v1/**")
                .packagesToScan("br.com.optimus.api.controller")
                .addOpenApiCustomizer(this.openApiCustomiser())
                .build();
    }

    @Bean
    public OpenAPI openApiBean() {
        return new OpenAPI()
                .info(new Info().title(System.getenv("titleApi") != null ? System.getenv("titleApi") : "Optimus API")
                        .description(descriptionApp)
                        .version("1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Optimus Solucoes e Tecnologia, eribeiro18@gmail.com")
                        .url("https://optimussolucoes.com.br/"));
    }

    @Bean
    public OpenApiCustomizer openApiCustomiser() {
        return openApi -> openApi.getPaths().values().forEach(path ->
                path.readOperations().forEach(operation -> {
                    operation.setDescription(
                            "Protected path. Requires Bearer JWT token obtained via /v1/login."
                    );
                    buildResponses(operation);
                })
        );
    }

    private ApiResponses buildResponses(Operation operation) {
        ApiResponses apiResponses = operation.getResponses();
        apiResponses.addApiResponse("200", fnApiResponse.apply("OK"));
        apiResponses.addApiResponse("201", fnApiResponse.apply("Created"));
        apiResponses.addApiResponse("202", fnApiResponse.apply("Accepted"));
        apiResponses.addApiResponse("204", fnApiResponse.apply("No Content"));
        apiResponses.addApiResponse("301", fnApiResponse.apply("Moved Permanently"));
        apiResponses.addApiResponse("400", fnApiResponse.apply("Bad Request"));
        apiResponses.addApiResponse("401", fnApiResponse.apply("Unauthorized access"));
        apiResponses.addApiResponse("403", fnApiResponse.apply("Forbidden"));
        apiResponses.addApiResponse("404", fnApiResponse.apply("Not found"));
        apiResponses.addApiResponse("409", fnApiResponse.apply("Request problems"));
        apiResponses.addApiResponse("500", fnApiResponse.apply("Internal server error"));

        return apiResponses;
    }
}

