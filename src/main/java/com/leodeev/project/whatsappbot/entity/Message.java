package com.leodeev.project.whatsappbot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa un Mensaje
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Relación: Un mensaje pertenece a una conversación
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;
    
    @Column(name = "whatsapp_message_id", unique = true, length = 100)
    private String whatsappMessageId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MessageType type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MessageDirection direction;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    
    @Column(name = "sender_phone", length = 20)
    private String senderPhone;
    
    @Column(name = "recipient_phone", length = 20)
    private String recipientPhone;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private MessageStatus status = MessageStatus.SENT;
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(name = "sent_at")
    private LocalDateTime sentAt;
    
    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;
    
    @Column(name = "read_at")
    private LocalDateTime readAt;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (sentAt == null) {
            sentAt = LocalDateTime.now();
        }
        if (status == null) {
            status = MessageStatus.SENT;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * Tipo de mensaje
     */
    public enum MessageType {
        TEXT,        // Mensaje de texto
        IMAGE,       // Imagen
        DOCUMENT,    // Documento
        AUDIO,       // Audio
        VIDEO,       // Video
        LOCATION,    // Ubicación
        TEMPLATE     // Plantilla de WhatsApp
    }
    
    /**
     * Dirección del mensaje
     */
    public enum MessageDirection {
        INBOUND,     // Mensaje recibido (del cliente)
        OUTBOUND     // Mensaje enviado (del bot)
    }
    
    /**
     * Estado del mensaje
     */
    public enum MessageStatus {
        SENT,        // Enviado
        DELIVERED,   // Entregado
        READ,        // Leído
        FAILED       // Falló
    }
}
