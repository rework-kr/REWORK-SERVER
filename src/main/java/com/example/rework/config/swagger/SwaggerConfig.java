package com.example.rework.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@OpenAPIDefinition(
        info = @Info(title = "Rework API 명세서",
                description = "Rework API 명세서",
                version = "v1"))
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(){
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        Server server = new Server();
        server.setUrl("https://api.rework.im");
        server.setDescription("배포 서버");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Arrays.asList(securityRequirement))
                .servers(Arrays.asList(server))
                .path("/api/v1/members/login", new PathItem()
                        .post(new Operation()
                                .operationId("login")
                                .tags(Arrays.asList("Authentication")) // 태그 지정
                                .description("사용자 인증을 위한 로그인 엔드포인트") // 설명 추가
                                .requestBody(new RequestBody()
                                        .content(new Content()
                                                .addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                                        new MediaType().schema(new ObjectSchema()
                                                                .addProperties("userId", new StringSchema())
                                                                .addProperties("password", new StringSchema())))))
                                .responses(new ApiResponses()
                                        .addApiResponse("200", new ApiResponse().description("인증 성공"))
                                        .addApiResponse("403", new ApiResponse().description("인증 실패")))));
    }
}
