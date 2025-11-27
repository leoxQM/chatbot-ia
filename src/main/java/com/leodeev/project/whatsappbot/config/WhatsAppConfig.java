package com.leodeev.project.whatsappbot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de WhatsApp Business API
 * Lee las propiedades desde application.properties con prefijo "whatsapp"
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "whatsapp")
public class WhatsAppConfig {
    
    private Phone phone;
    private Business business;
    private String accessToken;
    private Api api;
    private Webhook webhook;
    
    @Data
    public static class Phone {
        private Number number;
        
        @Data
        public static class Number {
            private String id;
        }
    }
    
    @Data
    public static class Business {
        private Account account;
        
        @Data
        public static class Account {
            private String id;
        }
    }
    
    @Data
    public static class Api {
        private Base base;
        
        @Data
        public static class Base {
            private String url;
        }
    }
    
    @Data
    public static class Webhook {
        private Verify verify;
        
        @Data
        public static class Verify {
            private String token;
        }
    }
}
