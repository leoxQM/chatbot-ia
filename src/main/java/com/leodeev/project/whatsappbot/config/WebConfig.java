package com.leodeev.project.whatsappbot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de CORS y Web
 * Lee las propiedades desde application.properties con prefijo "cors"
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cors")
public class WebConfig implements WebMvcConfigurer {

    private Allowed allowed;
    private Allow allow;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (allowed != null && allowed.getOrigins() != null) {
            registry.addMapping("/**")
                    .allowedOrigins(allowed.getOrigins().split(","))
                    .allowedMethods(allowed.getMethods().split(","))
                    .allowedHeaders(allowed.getHeaders().split(","))
                    .allowCredentials(allow != null && allow.getCredentials() != null ? allow.getCredentials() : false);
        }
    }

    @Data
    public static class Allowed {
        private String origins;
        private String methods;
        private String headers;
    }

    @Data
    public static class Allow {
        private Boolean credentials;
    }
}
