package com.senabo.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "SENABO API 명세서",
                description = "보호자 교육 서비스 API 명세서",
                version = "v1"))
@Configuration
public class SwaggerConfig {


}