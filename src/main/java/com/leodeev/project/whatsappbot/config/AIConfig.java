package com.leodeev.project.whatsappbot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuración de servicios de IA (Claude, OpenAI)
 * Lee las propiedades desde application.properties con prefijo "ai"
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ai")
public class AIConfig {
    
    private String provider;
    private Claude claude;
    private Openai openai;
    
    @Data
    public static class Claude {
        private Api api;
        private String model;
        private Max max;
        
        @Data
        public static class Api {
            private String key;
            private String url;
        }
        
        @Data
        public static class Max {
            private Integer tokens;
        }
    }
    
    @Data
    public static class Openai {
        private Api api;
        private String model;
        
        @Data
        public static class Api {
            private String key;
            private String url;
        }
    }
    
    /**
     * Bean de WebClient para hacer llamadas HTTP a las APIs de IA
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
