package com.leodeev.project.whatsappbot.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para recibir webhooks de WhatsApp
 * Estructura según la API de WhatsApp Business
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WhatsAppWebhookRequest {
    
    @JsonProperty("object")
    private String object;
    
    @JsonProperty("entry")
    private List<Entry> entry;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Entry {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("changes")
        private List<Change> changes;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Change {
        @JsonProperty("value")
        private Value value;
        
        @JsonProperty("field")
        private String field;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Value {
        @JsonProperty("messaging_product")
        private String messagingProduct;
        
        @JsonProperty("metadata")
        private Metadata metadata;
        
        @JsonProperty("contacts")
        private List<Contact> contacts;
        
        @JsonProperty("messages")
        private List<IncomingMessage> messages;
        
        @JsonProperty("statuses")
        private List<Status> statuses;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Metadata {
        @JsonProperty("display_phone_number")
        private String displayPhoneNumber;
        
        @JsonProperty("phone_number_id")
        private String phoneNumberId;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Contact {
        @JsonProperty("profile")
        private Profile profile;
        
        @JsonProperty("wa_id")
        private String waId;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Profile {
        @JsonProperty("name")
        private String name;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IncomingMessage {
        @JsonProperty("from")
        private String from;
        
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("timestamp")
        private String timestamp;
        
        @JsonProperty("type")
        private String type;
        
        @JsonProperty("text")
        private TextMessage text;
        
        @JsonProperty("image")
        private MediaMessage image;
        
        @JsonProperty("document")
        private MediaMessage document;
        
        @JsonProperty("audio")
        private MediaMessage audio;
        
        @JsonProperty("video")
        private MediaMessage video;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TextMessage {
        @JsonProperty("body")
        private String body;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MediaMessage {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("mime_type")
        private String mimeType;
        
        @JsonProperty("sha256")
        private String sha256;

        @JsonProperty("caption")
        private String caption;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Status {
        @JsonProperty("id")
        private String id;
        @JsonProperty("status")
        private String status;
        @JsonProperty("timestamp")
        private String timestamp;
        @JsonProperty("recipient_id")
        private String recipientId;
    }
}
