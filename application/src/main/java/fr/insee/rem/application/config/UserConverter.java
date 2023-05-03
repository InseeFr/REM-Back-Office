package fr.insee.rem.application.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class UserConverter extends ClassicConverter {

    private static final String ANONYMOUS_USER = "anonymousUser";

    @Override
    public String convert(ILoggingEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return "";
        }

        if (auth.getPrincipal() != null && ANONYMOUS_USER.equals(auth.getPrincipal())) {
            return "NOAUTH";
        }

        return ((Jwt) auth.getCredentials()).getClaims().get("preferred_username").toString().toUpperCase();
    }
}
