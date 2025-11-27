package com.leodeev.project.whatsappbot.repository;

import com.leodeev.project.whatsappbot.entity.Message;
import com.leodeev.project.whatsappbot.entity.Message.MessageDirection;
import com.leodeev.project.whatsappbot.entity.Message.MessageStatus;
import com.leodeev.project.whatsappbot.entity.Message.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Message
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    /**
     * Buscar mensajes por ID de conversación
     */
    List<Message> findByConversationId(Long conversationId);
    
    /**
     * Buscar mensajes por ID de conversación ordenados por fecha
     */
    @Query("SELECT m FROM Message m WHERE m.conversation.id = :conversationId ORDER BY m.sentAt ASC")
    List<Message> findByConversationIdOrderBySentAt(@Param("conversationId") Long conversationId);
    
    /**
     * Buscar mensaje por ID de WhatsApp
     */
    Optional<Message> findByWhatsappMessageId(String whatsappMessageId);
    
    /**
     * Buscar mensajes por dirección
     */
    List<Message> findByDirection(MessageDirection direction);
    
    /**
     * Buscar mensajes por estado
     */
    List<Message> findByStatus(MessageStatus status);
    
    /**
     * Buscar mensajes por tipo
     */
    List<Message> findByType(MessageType type);
    
    /**
     * Buscar mensajes fallidos
     */
    @Query("SELECT m FROM Message m WHERE m.status = 'FAILED' ORDER BY m.sentAt DESC")
    List<Message> findFailedMessages();
    
    /**
     * Buscar mensajes en un rango de fechas
     */
    @Query("SELECT m FROM Message m WHERE m.sentAt BETWEEN :startDate AND :endDate ORDER BY m.sentAt DESC")
    List<Message> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                  @Param("endDate") LocalDateTime endDate);
    
    /**
     * Contar mensajes por conversación
     */
    long countByConversationId(Long conversationId);
    
    /**
     * Buscar últimos N mensajes de una conversación
     */
    @Query("SELECT m FROM Message m WHERE m.conversation.id = :conversationId ORDER BY m.sentAt DESC")
    List<Message> findLastMessagesByConversation(@Param("conversationId") Long conversationId);
    
    /**
     * Buscar mensajes entrantes de un teléfono
     */
    @Query("SELECT m FROM Message m WHERE m.senderPhone = :phoneNumber AND m.direction = 'INBOUND' ORDER BY m.sentAt DESC")
    List<Message> findInboundMessagesByPhone(@Param("phoneNumber") String phoneNumber);
}
