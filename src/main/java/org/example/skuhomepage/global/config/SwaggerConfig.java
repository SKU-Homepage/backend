package org.example.skuhomepage.global.config;

import java.util.Arrays;

import org.example.skuhomepage.global.annotation.ApiErrorCodeExample;
import org.example.skuhomepage.global.apiPayload.ApiResponse;
import org.example.skuhomepage.global.apiPayload.code.BaseErrorCode;
import org.example.skuhomepage.global.apiPayload.code.ErrorReasonDTO;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

  @Value("${server.servlet.context-path:}")
  private String contextPath;

  @Bean
  public OpenAPI customOpenAPI() {
    Server localServer = new Server();
    localServer.setUrl(contextPath);
    localServer.setDescription("Local Server");

    return new OpenAPI()
        .addServersItem(localServer)
        //        .addServersItem(prodServer)
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(
            new Components()
                .addSecuritySchemes(
                    "bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
        .info(new Info().title("틀딱즈 명세서").version("1.0").description("틀딱즈 Swagger"));
  }

  @Bean
  public GroupedOpenApi customGroupedOpenApi() {
    return GroupedOpenApi.builder()
        .group("api")
        .pathsToMatch("/**")
        .addOperationCustomizer(customize())
        .build();
  }

  @Bean
  public OperationCustomizer customize() {
    return (operation, handlerMethod) -> {
      ApiErrorCodeExample annotation = handlerMethod.getMethodAnnotation(ApiErrorCodeExample.class);
      if (annotation != null) {
        generateErrorCodeResponseExample(operation, annotation.value());
      }
      return operation;
    };
  }

  private void generateErrorCodeResponseExample(
      Operation operation, Class<? extends BaseErrorCode> errorCodeClass) {
    ApiResponses responses = operation.getResponses();
    BaseErrorCode[] errorCodes = errorCodeClass.getEnumConstants();

    Arrays.stream(errorCodes)
        .forEach(
            errorCode -> {
              // 에러 이유 가져오기
              ErrorReasonDTO reason = errorCode.getReasonHttpStatus();
              ApiResponse<Object> example = createExample(reason);

              // ApiResponse 생성
              io.swagger.v3.oas.models.responses.ApiResponse apiResponse =
                  responses.computeIfAbsent(
                      String.valueOf(reason.getHttpStatus().value()),
                      status ->
                          new io.swagger.v3.oas.models.responses.ApiResponse()
                              .description("Error Responses")
                              .content(new Content()));

              Content content = apiResponse.getContent();
              if (!content.containsKey("application/json")) {
                content.addMediaType("application/json", new MediaType());
              }

              MediaType mediaType = content.get("application/json");
              mediaType.addExamples(reason.getCode(), createSwaggerExample(example));
            });
  }

  private Example createSwaggerExample(ApiResponse<?> apiResponse) {
    Example example = new Example();
    example.setValue(apiResponse);
    return example;
  }

  private ApiResponse<Object> createExample(ErrorReasonDTO errorReason) {
    return ApiResponse.onFailure(errorReason.getCode(), errorReason.getMessage(), null);
  }
}
