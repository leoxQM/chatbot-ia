package com.leodeev.project.whatsappbot.service;

import com.leodeev.project.whatsappbot.dto.response.AIResponse;

import java.util.List;

/**
 * Interfaz del servicio de IA
 * SOLID: Open/Closed Principle (OCP) - Permite múltiples implementaciones
 * SOLID: Dependency Inversion Principle (DIP)
 */
public interface AIService {
    
    /**
     * Generar respuesta de IA basada en el mensaje del usuario
     * @param userMessage Mensaje del usuario
     * @param conversationHistory Historial de conversación (opcional)
     * @return Respuesta de la IA
     */
    AIResponse generateResponse(String userMessage, List<String> conversationHistory);
    
    /**
     * Generar respuesta de IA con contexto de productos
     * @param userMessage Mensaje del usuario
     * @param productsContext Contexto de productos disponibles
     * @return Respuesta de la IA
     */
    AIResponse generateResponseWithProducts(String userMessage, String productsContext);
    
    /**
     * Buscar productos relevantes según la consulta del usuario
     * @param userQuery Consulta del usuario
     * @return Información de productos relevantes en formato texto
     */
    String searchRelevantProducts(String userQuery);
}
