package com.leodeev.project.whatsappbot.mapper;

import com.leodeev.project.whatsappbot.entity.Conversation;
import com.leodeev.project.whatsappbot.entity.Message;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre Message Entity y DTOs
 * 
 * Nota: No hay MessageResponse en la estructura original,
 * pero este mapper es útil para crear entities desde webhooks
 */
@Component
public class MessageMapper {
    /**
     * Crea un Message Entity desde datos del webhook de WhatsApp
     * @param whatsappMessageId ID del mensaje de WhatsApp
     * @param content Contenido del mensaje
     * @param senderPhone Teléfono del remitente
     * @param conversation Conversación a la que pertenece
     * @return Message entity
     */
    public Message createInboundMessage(
            String whatsappMessageId,
            String content,
            String senderPhone,
            Conversation conversation) {

        return Message.builder()
                .conversation(conversation)
                .whatsappMessageId(whatsappMessageId)
                .type(Message.MessageType.TEXT)
                .direction(Message.MessageDirection.INBOUND)
                .content(content)
                .senderPhone(senderPhone)
                .recipientPhone(null) // Se puede agregar si es necesario
                .status(Message.MessageStatus.DELIVERED)
                .build();
    }

    /**
     * Crea un Message Entity para enviar (outbound)
     * @param content Contenido del mensaje
     * @param recipientPhone Teléfono del destinatario
     * @param conversation Conversación a la que pertenece
     * @return Message entity
     */
    public Message createOutboundMessage(
            String content,
            String recipientPhone,
            Conversation conversation) {

        return Message.builder()
                .conversation(conversation)
                .type(Message.MessageType.TEXT)
                .direction(Message.MessageDirection.OUTBOUND)
                .content(content)
                .senderPhone(null) // Número del bot
                .recipientPhone(recipientPhone)
                .status(Message.MessageStatus.SENT)
                .build();
    }

    /**
     * Crea un Message Entity con tipo específico
     * @param whatsappMessageId ID del mensaje de WhatsApp
     * @param content Contenido del mensaje
     * @param senderPhone Teléfono del remitente
     * @param messageType Tipo de mensaje (TEXT, IMAGE, etc.)
     * @param conversation Conversación a la que pertenece
     * @return Message entity
     */
    public Message createInboundMessageWithType(
            String whatsappMessageId,
            String content,
            String senderPhone,
            Message.MessageType messageType,
            Conversation conversation) {

        return Message.builder()
                .conversation(conversation)
                .whatsappMessageId(whatsappMessageId)
                .type(messageType)
                .direction(Message.MessageDirection.INBOUND)
                .content(content)
                .senderPhone(senderPhone)
                .status(Message.MessageStatus.DELIVERED)
                .build();
    }

    /**
     * Actualiza el estado de un mensaje
     * @param message Mensaje a actualizar
     * @param status Nuevo estado
     */
    public void updateMessageStatus(Message message, Message.MessageStatus status) {
        if (message == null) {
            return;
        }
        message.setStatus(status);
    }

    /**
     * Actualiza el whatsappMessageId después de enviar
     * @param message Mensaje a actualizar
     * @param whatsappMessageId ID devuelto por WhatsApp
     */
    public void updateWhatsAppMessageId(Message message, String whatsappMessageId) {
        if (message == null) {
            return;
        }
        message.setWhatsappMessageId(whatsappMessageId);
    }
}





