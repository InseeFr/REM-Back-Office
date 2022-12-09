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

    @Value("${fr.insee.rem.keycloak.auth-server-url}")
    private String keycloakUrl;

    @Value("${fr.insee.rem.keycloak.realm}")
    private String keycloakRealm;

    @Value("${fr.insee.rem.role.administrator}")
    private String roleAdmin;

    @Value("#{'${fr.insee.rem.security.whitelist-matchers}'.split(',')}")
    private String[] whiteList;

    @Value("${fr.insee.rem.security.auth.mode}")
    private String authMode;

    @Value("${fr.insee.rem.host}")
    private String host;

}
