package fr.insee.rem.application.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SpringDocConfiguration {

    @Bean
    OpenAPI customOpenAPI(PropertiesConfiguration props) {
        if (!"OIDC".equals(props.getAuthMode())) {
            // noauth
            return new OpenAPI().info(new Info().title(props.getSpringDocTitle())
                .description(props.getSpringDocDescription()).version(props.getVersion()));
        }

        String authUrl = props.getKeycloakUrl() + "/realms/" + props.getKeycloakRealm() + "/protocol/openid-connect";

        return new OpenAPI().addServersItem(new Server().url(props.getHost()))
            .components(new Components().addSecuritySchemes("oauth2", new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2).flows(getFlows(authUrl))))
            .info(new Info().title(props.getSpringDocTitle()).description(props.getSpringDocDescription())
                .version(props.getVersion()))
            .addSecurityItem(new SecurityRequirement().addList("oauth2", Arrays.asList("read", "write")));
    }

    private OAuthFlows getFlows(String authUrl) {
        OAuthFlows flows = new OAuthFlows();
        OAuthFlow flow = new OAuthFlow();
        Scopes scopes = new Scopes();
        flow.setAuthorizationUrl(authUrl + "/auth");
        flow.setTokenUrl(authUrl + "/token");
        flow.setRefreshUrl(authUrl + "/token");
        flow.setScopes(scopes);
        flows = flows.authorizationCode(flow);
        return flows;
    }

}