package com.leodeev.project.whatsappbot.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO de respuesta de la API de WhatsApp al enviar mensajes
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WhatsAppMessageResponse {
    
    @JsonProperty("messaging_product")
    private String messagingProduct;
    
    @JsonProperty("contacts")
    private List<Contact> contacts;
    
    @JsonProperty("messages")
    private List<Message> messages;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Contact {
        @JsonProperty("input")
        private String input;
        
        @JsonProperty("wa_id")
        private String waId;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        @JsonProperty("id")
        private String id;
    }
}
