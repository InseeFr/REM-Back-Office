package fr.insee.rem.application.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "fr.insee.rem.security.auth.mode", havingValue = "OIDC")
public class SecurityConfiguration {

    // Par défaut, spring sécurity prefixe les rôles avec cette chaine
    private static final String ROLE_PREFIX = "ROLE_";

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, PropertiesConfiguration props) throws Exception {
        http.csrf().disable() //NOSONAR
            .authorizeHttpRequests()
            .requestMatchers(props.getWhiteList())
            .permitAll()
            .requestMatchers(HttpMethod.GET)
            .hasAnyRole(props.getRoleUser(), props.getRoleAdmin()).anyRequest().hasAnyRole(props.getRoleAdmin()).and()
            .oauth2ResourceServer(oauth2 -> oauth2.jwt().jwtAuthenticationConverter(jwtAuthenticationConverter()));
        return http.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setPrincipalClaimName("name");
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
        return jwtAuthenticationConverter;
    }

    Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
        return new Converter<Jwt, Collection<GrantedAuthority>>() {
            @SuppressWarnings("unchecked")
            @Override
            public Collection<GrantedAuthority> convert(Jwt jwt) {
                Map<String, Object> claims = jwt.getClaims();
                Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");
                List<String> roles = (List<String>) realmAccess.get("roles");

                return roles.stream().map(r -> new SimpleGrantedAuthority(ROLE_PREFIX + r)).collect(Collectors
                    .toCollection(ArrayList::new));
            }
        };
    }
}
