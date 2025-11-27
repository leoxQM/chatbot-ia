package com.leodeev.project.whatsappbot.service.serviceImpl;

import com.leodeev.project.whatsappbot.dto.response.ConversationResponse;
import com.leodeev.project.whatsappbot.entity.Conversation;
import com.leodeev.project.whatsappbot.entity.Customer;
import com.leodeev.project.whatsappbot.exception.ResourceNotFoundException;
import com.leodeev.project.whatsappbot.mapper.ConversationMapper;
import com.leodeev.project.whatsappbot.repository.ConversationRepository;
import com.leodeev.project.whatsappbot.service.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de Conversaciones
 * SOLID: Single Responsibility Principle - Solo maneja lógica de conversaciones
 * SOLID: Dependency Inversion - Depende de interfaces (Repository, Mapper)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    
    private final ConversationRepository conversationRepository;
    private final ConversationMapper conversationMapper;
    
    /**
     * Crear una nueva conversación
     */
    @Override
    @Transactional
    public Conversation createConversation(Customer customer) {
        log.info("Creating new conversation for customer ID: {}", customer.getId());
        
        Conversation conversation = Conversation.builder()
                .customer(customer)
                .status(Conversation.ConversationStatus.ACTIVE)
                .startedAt(LocalDateTime.now())
                .build();
        
        Conversation savedConversation = conversationRepository.save(conversation);
        
        log.info("Conversation created successfully with ID: {}", savedConversation.getId());
        
        return savedConversation;
    }
    
    /**
     * Obtener conversación activa de un cliente
     */
    @Override
    @Transactional(readOnly = true)
    public Conversation getActiveConversation(Long customerId) {
        log.info("Fetching active conversation for customer ID: {}", customerId);
        
        Optional<Conversation> conversation = conversationRepository
                .findActiveConversationByCustomerId(customerId);
        
        return conversation.orElse(null);
    }
    
    /**
     * Obtener o crear conversación activa para un cliente
     */
    @Override
    @Transactional
    public Conversation getOrCreateActiveConversation(Customer customer) {
        log.info("Getting or creating active conversation for customer ID: {}", customer.getId());
        
        // Buscar conversación activa existente
        Conversation activeConversation = getActiveConversation(customer.getId());
        
        // Si existe, retornarla
        if (activeConversation != null) {
            log.info("Found existing active conversation with ID: {}", activeConversation.getId());
            return activeConversation;
        }
        
        // Si no existe, crear una nueva
        log.info("No active conversation found, creating new one");
        return createConversation(customer);
    }
    
    /**
     * Obtener conversación por ID
     */
    @Override
    @Transactional(readOnly = true)
    public ConversationResponse getConversationById(Long id) {
        log.info("Fetching conversation with ID: {}", id);
        
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conversación no encontrada con ID: " + id));
        
        return conversationMapper.toResponse(conversation);
    }
    
    /**
     * Obtener todas las conversaciones de un cliente
     */
    @Override
    @Transactional(readOnly = true)
    public List<ConversationResponse> getConversationsByCustomerId(Long customerId) {
        log.info("Fetching conversations for customer ID: {}", customerId);
        
        List<Conversation> conversations = conversationRepository.findByCustomerId(customerId);
        
        return conversationMapper.toResponseList(conversations);
    }
    
    /**
     * Obtener conversaciones por número de teléfono
     */
    @Override
    @Transactional(readOnly = true)
    public List<ConversationResponse> getConversationsByPhoneNumber(String phoneNumber) {
        log.info("Fetching conversations for phone number: {}", phoneNumber);
        
        List<Conversation> conversations = conversationRepository
                .findByCustomerPhoneNumber(phoneNumber);
        
        return conversationMapper.toResponseList(conversations);
    }
    
    /**
     * Cerrar una conversación
     */
    @Override
    @Transactional
    public void closeConversation(Long conversationId) {
        log.info("Closing conversation with ID: {}", conversationId);
        
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversación no encontrada con ID: " + conversationId));
        
        conversation.setStatus(Conversation.ConversationStatus.CLOSED);
        conversation.setEndedAt(LocalDateTime.now());
        
        conversationRepository.save(conversation);
        
        log.info("Conversation closed successfully with ID: {}", conversationId);
    }
    
    /**
     * Actualizar tema de conversación
     */
    @Override
    @Transactional
    public void updateConversationTopic(Long conversationId, String topic) {
        log.info("Updating topic for conversation ID: {} to: {}", conversationId, topic);
        
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversación no encontrada con ID: " + conversationId));
        
        conversation.setTopic(topic);
        
        conversationRepository.save(conversation);
        
        log.info("Conversation topic updated successfully");
    }
}
