package com.leodeev.project.whatsappbot.service;

import com.leodeev.project.whatsappbot.dto.response.ConversationResponse;
import com.leodeev.project.whatsappbot.entity.Conversation;
import com.leodeev.project.whatsappbot.entity.Customer;

import java.util.List;

/**
 * Interfaz del servicio de Conversaciones
 * SOLID: Dependency Inversion Principle (DIP)
 */
public interface ConversationService {

    /**
     * Crear una nueva conversación
     * @param customer Cliente
     * @return Conversación creada
     */
    Conversation createConversation(Customer customer);

    /**
     * Obtener conversación activa de un cliente
     * @param customerId ID del cliente
     * @return Conversación activa o null
     */
    Conversation getActiveConversation(Long customerId);

    /**
     * Obtener o crear conversación activa para un cliente
     * @param customer Cliente
     * @return Conversación activa
     */
    Conversation getOrCreateActiveConversation(Customer customer);

    /**
     * Obtener conversación por ID
     * @param id ID de la conversación
     * @return Conversación encontrada
     */
    ConversationResponse getConversationById(Long id);

    /**
     * Obtener todas las conversaciones de un cliente
     * @param customerId ID del cliente
     * @return Lista de conversaciones
     */
    List<ConversationResponse> getConversationsByCustomerId(Long customerId);

    /**
     * Obtener conversaciones por número de teléfono
     * @param phoneNumber Número de teléfono
     * @return Lista de conversaciones
     */
    List<ConversationResponse> getConversationsByPhoneNumber(String phoneNumber);

    /**
     * Cerrar una conversación
     * @param conversationId ID de la conversación
     */
    void closeConversation(Long conversationId);

    /**
     * Actualizar tema de conversación
     * @param conversationId ID de la conversación
     * @param topic Nuevo tema
     */
    void updateConversationTopic(Long conversationId, String topic);
}
