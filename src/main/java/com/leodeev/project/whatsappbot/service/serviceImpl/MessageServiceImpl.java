package com.leodeev.project.whatsappbot.service.serviceImpl;


import com.leodeev.project.whatsappbot.entity.Conversation;
import com.leodeev.project.whatsappbot.entity.Message;
import com.leodeev.project.whatsappbot.exception.ResourceNotFoundException;
import com.leodeev.project.whatsappbot.mapper.MessageMapper;
import com.leodeev.project.whatsappbot.repository.MessageRepository;
import com.leodeev.project.whatsappbot.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio de Mensajes
 * SOLID: Single Responsibility Principle - Solo maneja lógica de mensajes
 * SOLID: Dependency Inversion - Depende de interfaces (Repository, Mapper)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    
    /**
     * Guardar un mensaje
     */
    @Override
    @Transactional
    public Message saveMessage(Message message) {
        log.info("Guardando mensaje en la base de datos");
        
        Message savedMessage = messageRepository.save(message);
        
        log.info("Mensaje guardado exitosamente con ID: {}", savedMessage.getId());
        
        return savedMessage;
    }
    
    /**
     * Crear y guardar mensaje entrante (del cliente)
     */
    @Override
    @Transactional
    public Message createInboundMessage(String whatsappMessageId, String content, String senderPhone, Conversation conversation) {
        log.info("Creando mensaje entrante desde teléfono: {}", senderPhone);
        
        // Crear mensaje usando el mapper
        Message message = messageMapper.createInboundMessage(
                whatsappMessageId,
                content,
                senderPhone,
                conversation
        );
        
        // Guardar en BD
        Message savedMessage = messageRepository.save(message);
        
        log.info("Mensaje entrante guardado con ID: {} para conversación ID: {}", 
                savedMessage.getId(), conversation.getId());
        
        return savedMessage;
    }
    
    /**
     * Crear y guardar mensaje saliente (del bot)
     */
    @Override
    @Transactional
    public Message createOutboundMessage(String content, String recipientPhone, Conversation conversation) {
        log.info("Creando mensaje saliente hacia teléfono: {}", recipientPhone);
        
        // Crear mensaje usando el mapper
        Message message = messageMapper.createOutboundMessage(
                content,
                recipientPhone,
                conversation
        );
        
        // Guardar en BD
        Message savedMessage = messageRepository.save(message);
        
        log.info("Mensaje saliente guardado con ID: {} para conversación ID: {}", 
                savedMessage.getId(), conversation.getId());
        
        return savedMessage;
    }
    
    /**
     * Obtener mensajes de una conversación
     */
    @Override
    @Transactional(readOnly = true)
    public List<Message> getMessagesByConversationId(Long conversationId) {
        log.info("Obteniendo mensajes de la conversación ID: {}", conversationId);
        
        List<Message> messages = messageRepository.findByConversationIdOrderBySentAt(conversationId);
        
        log.info("Se encontraron {} mensajes", messages.size());
        
        return messages;
    }
    
    /**
     * Actualizar estado de mensaje
     */
    @Override
    @Transactional
    public void updateMessageStatus(Long messageId, Message.MessageStatus status) {
        log.info("Actualizando estado del mensaje ID: {} a {}", messageId, status);
        
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Mensaje no encontrado con ID: " + messageId));
        
        // Actualizar estado usando el mapper
        messageMapper.updateMessageStatus(message, status);
        
        // Guardar cambios
        messageRepository.save(message);
        
        log.info("Estado del mensaje actualizado exitosamente");
    }
    
    /**
     * Actualizar WhatsApp Message ID
     */
    @Override
    @Transactional
    public void updateWhatsAppMessageId(Long messageId, String whatsappMessageId) {
        log.info("Actualizando WhatsApp Message ID para mensaje interno ID: {}", messageId);
        
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Mensaje no encontrado con ID: " + messageId));
        
        // Actualizar WhatsApp ID usando el mapper
        messageMapper.updateWhatsAppMessageId(message, whatsappMessageId);
        
        // Guardar cambios
        messageRepository.save(message);
        
        log.info("WhatsApp Message ID actualizado a: {}", whatsappMessageId);
    }
}
