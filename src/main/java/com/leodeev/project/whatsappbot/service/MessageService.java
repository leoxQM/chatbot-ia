package com.leodeev.project.whatsappbot.service;

import com.leodeev.project.whatsappbot.entity.Conversation;
import com.leodeev.project.whatsappbot.entity.Message;

import java.util.List;

/**
 * Interfaz del servicio de Mensajes
 * SOLID: Dependency Inversion Principle (DIP)
 */
public interface MessageService {

    /**
     * Guardar un mensaje
     * @param message Mensaje a guardar
     * @return Mensaje guardado
     */
    Message saveMessage(Message message);

    /**
     * Crear y guardar mensaje entrante (del cliente)
     * @param whatsappMessageId ID del mensaje de WhatsApp
     * @param content Contenido
     * @param senderPhone Teléfono del remitente
     * @param conversation Conversación
     * @return Mensaje guardado
     */
    Message createInboundMessage(String whatsappMessageId, String content, String senderPhone, Conversation conversation);

    /**
     * Crear y guardar mensaje saliente (del bot)
     * @param content Contenido
     * @param recipientPhone Teléfono del destinatario
     * @param conversation Conversación
     * @return Mensaje guardado
     */
    Message createOutboundMessage(String content, String recipientPhone, Conversation conversation);

    /**
     * Obtener mensajes de una conversación
     * @param conversationId ID de la conversación
     * @return Lista de mensajes
     */
    List<Message> getMessagesByConversationId(Long conversationId);

    /**
     * Actualizar estado de mensaje
     * @param messageId ID del mensaje
     * @param status Nuevo estado
     */
    void updateMessageStatus(Long messageId, Message.MessageStatus status);

    /**
     * Actualizar WhatsApp Message ID
     * @param messageId ID interno del mensaje
     * @param whatsappMessageId ID de WhatsApp
     */
    void updateWhatsAppMessageId(Long messageId, String whatsappMessageId);
}
