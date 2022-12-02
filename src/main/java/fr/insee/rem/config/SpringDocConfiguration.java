package fr.insee.rem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class SpringDocConfiguration {

    @Bean
    public OpenAPI customOpenAPIBasicAndOIDC(PropertiesConfiguration props) {
        return new OpenAPI().info(new Info().title(props.getSpringDocTitle()).description(props.getSpringDocDescription()).version(props.getVersion()));
    }

}