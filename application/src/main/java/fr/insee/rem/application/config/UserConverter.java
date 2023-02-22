package fr.insee.rem.application.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class UserConverter extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return ((Jwt) auth.getCredentials()).getClaims().get("preferred_username").toString().toUpperCase();
        }
        return "";
    }
}