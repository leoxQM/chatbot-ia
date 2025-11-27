package com.leodeev.project.whatsappbot.controller;

import com.leodeev.project.whatsappbot.dto.request.MessageRequest;
import com.leodeev.project.whatsappbot.dto.request.WhatsAppWebhookRequest;
import com.leodeev.project.whatsappbot.dto.response.WhatsAppMessageResponse;
import com.leodeev.project.whatsappbot.service.WhatsAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para Webhooks de WhatsApp
 * SOLID: Single Responsibility Principle - Solo maneja webhooks de WhatsApp
 * SOLID: Dependency Inversion - Depende de WhatsAppService (interface)
 * 
 * Este es el controlador MÁS IMPORTANTE del chatbot
 */
@Slf4j
@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
public class WebhookController {
    
    private final WhatsAppService whatsAppService;
    
    /**
     * Verificar webhook (GET request de WhatsApp)
     * WhatsApp envía este request para verificar que el webhook es válido
     * 
     * GET /api/webhook?hub.mode=subscribe&hub.verify_token=TOKEN&hub.challenge=CHALLENGE
     */
    @GetMapping
    public ResponseEntity<String> verifyWebhook(
            @RequestParam(name = "hub.mode") String mode,
            @RequestParam(name = "hub.verify_token") String token,
            @RequestParam(name = "hub.challenge") String challenge) {
        
        log.info("Solicitud de verificación de webhook recibida");
        log.debug("Mode: {}, Token: {}, Challenge: {}", mode, token, challenge);
        
        String result = whatsAppService.verifyWebhook(mode, token, challenge);
        
        if (result != null) {
            log.info("Webhook verificado exitosamente");
            return ResponseEntity.ok(result);
        }
        
        log.warn("Verificación de webhook fallida");
        return ResponseEntity.status(403).body("Forbidden");
    }
    
    /**
     * Recibir webhook (POST request de WhatsApp)
     * WhatsApp envía este request cuando hay mensajes nuevos
     * 
     * POST /api/webhook
     */
    @PostMapping
    public ResponseEntity<String> receiveWebhook(@RequestBody WhatsAppWebhookRequest request) {
        log.info("Webhook recibido de WhatsApp");
        
        try {
            // Procesar webhook de forma asíncrona para responder rápido a WhatsApp
            whatsAppService.processWebhook(request);
            
            // WhatsApp espera un 200 OK rápido
            return ResponseEntity.ok("EVENT_RECEIVED");
            
        } catch (Exception e) {
            log.error("Error procesando webhook: {}", e.getMessage(), e);
            // Aún así retornar 200 para que WhatsApp no reintente
            return ResponseEntity.ok("EVENT_RECEIVED");
        }
    }
    
    /**
     * Enviar mensaje manual (para testing o envío directo desde API)
     * 
     * POST /api/webhook/send
     */
    @PostMapping("/send")
    public ResponseEntity<WhatsAppMessageResponse> sendMessage(@Valid @RequestBody MessageRequest request) {
        log.info("Solicitud para enviar mensaje manual a: {}", request.getPhoneNumber());
        
        WhatsAppMessageResponse response = whatsAppService.sendMessage(request);
        
        return ResponseEntity.ok(response);
    }
}
