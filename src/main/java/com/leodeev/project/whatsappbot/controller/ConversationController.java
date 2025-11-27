
package com.leodeev.project.whatsappbot.controller;

import com.leodeev.project.whatsappbot.dto.response.ConversationResponse;
import com.leodeev.project.whatsappbot.service.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de Conversaciones
 * SOLID: Single Responsibility Principle - Solo maneja endpoints de conversaciones
 * SOLID: Dependency Inversion - Depende de ConversationService (interface)
 */
@Slf4j
@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {
    
    private final ConversationService conversationService;
    
    /**
     * Obtener conversación por ID
     * GET /api/conversations/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConversationResponse> getConversationById(@PathVariable Long id) {
        log.info("Solicitud para obtener conversación con ID: {}", id);
        
        ConversationResponse response = conversationService.getConversationById(id);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener conversaciones de un cliente por ID del cliente
     * GET /api/conversations/customer/{customerId}
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ConversationResponse>> getConversationsByCustomerId(@PathVariable Long customerId) {
        log.info("Solicitud para obtener conversaciones del cliente ID: {}", customerId);
        
        List<ConversationResponse> response = conversationService.getConversationsByCustomerId(customerId);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener conversaciones por número de teléfono
     * GET /api/conversations/phone/{phoneNumber}
     */
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<List<ConversationResponse>> getConversationsByPhoneNumber(@PathVariable String phoneNumber) {
        log.info("Solicitud para obtener conversaciones del teléfono: {}", phoneNumber);
        
        List<ConversationResponse> response = conversationService.getConversationsByPhoneNumber(phoneNumber);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Cerrar una conversación
     * PUT /api/conversations/{id}/close
     */
    @PutMapping("/{id}/close")
    public ResponseEntity<Void> closeConversation(@PathVariable Long id) {
        log.info("Solicitud para cerrar conversación con ID: {}", id);
        
        conversationService.closeConversation(id);
        
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Actualizar tema de conversación
     * PUT /api/conversations/{id}/topic
     */
    @PutMapping("/{id}/topic")
    public ResponseEntity<Void> updateConversationTopic(
            @PathVariable Long id,
            @RequestParam String topic) {
        
        log.info("Solicitud para actualizar tema de conversación ID: {} a: {}", id, topic);
        
        conversationService.updateConversationTopic(id, topic);
        
        return ResponseEntity.noContent().build();
    }
}
