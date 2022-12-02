package fr.insee.rem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class PropertiesConfiguration {

    @Value("${fr.insee.rem.version}")
    private String version;

    @Value("${fr.insee.rem.springdoc.description:Survey unit repository}")
    private String springDocDescription;

    @Value("${fr.insee.rem.springdoc.title:REM Api}")
    private String springDocTitle;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String keycloakUrl;

    @Value("${fr.insee.rem.role.administrator}")
    private String roleAdmin;

    @Value("#{'${fr.insee.rem.security.whitelist-matchers}'.split(',')}")
    private String[] whiteList;

}
