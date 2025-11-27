package com.leodeev.project.whatsappbot.service;

import com.leodeev.project.whatsappbot.dto.request.MessageRequest;
import com.leodeev.project.whatsappbot.dto.request.WhatsAppWebhookRequest;
import com.leodeev.project.whatsappbot.dto.response.WhatsAppMessageResponse;

/**
 * Interfaz del servicio de WhatsApp
 * SOLID: Dependency Inversion Principle (DIP)
 */
public interface WhatsAppService {

    /**
     * Procesar webhook entrante de WhatsApp
     * @param request Webhook recibido
     */
    void processWebhook(WhatsAppWebhookRequest request);

    /**
     * Enviar mensaje de texto a WhatsApp
     * @param phoneNumber Número de teléfono destino
     * @param message Mensaje a enviar
     * @return Respuesta de WhatsApp API
     */
    WhatsAppMessageResponse sendTextMessage(String phoneNumber, String message);

    /**
     * Enviar mensaje manual (desde API REST)
     * @param request Request con datos del mensaje
     * @return Respuesta de WhatsApp API
     */
    WhatsAppMessageResponse sendMessage(MessageRequest request);

    /**
     * Verificar webhook (GET request de WhatsApp)
     * @param mode Modo de verificación
     * @param token Token de verificación
     * @param challenge Challenge de WhatsApp
     * @return Challenge si es válido, null si no
     */
    String verifyWebhook(String mode, String token, String challenge);
}
