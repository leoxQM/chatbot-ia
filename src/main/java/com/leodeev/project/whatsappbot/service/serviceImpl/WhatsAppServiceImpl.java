package com.leodeev.project.whatsappbot.service.serviceImpl;

import com.leodeev.project.whatsappbot.config.WhatsAppConfig;
import com.leodeev.project.whatsappbot.dto.request.MessageRequest;
import com.leodeev.project.whatsappbot.dto.request.WhatsAppWebhookRequest;
import com.leodeev.project.whatsappbot.dto.response.WhatsAppMessageResponse;
import com.leodeev.project.whatsappbot.entity.Conversation;
import com.leodeev.project.whatsappbot.entity.Customer;
import com.leodeev.project.whatsappbot.entity.Message;
import com.leodeev.project.whatsappbot.exception.WhatsAppException;
import com.leodeev.project.whatsappbot.repository.CustomerRepository;
import com.leodeev.project.whatsappbot.service.AIService;
import com.leodeev.project.whatsappbot.service.ConversationService;
import com.leodeev.project.whatsappbot.service.MessageService;
import com.leodeev.project.whatsappbot.service.WhatsAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementación del servicio de WhatsApp
 * SOLID: Single Responsibility Principle - Solo maneja lógica de WhatsApp
 * SOLID: Dependency Inversion - Depende de interfaces
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WhatsAppServiceImpl implements WhatsAppService {
    
    private final WhatsAppConfig whatsAppConfig;
    private final CustomerRepository customerRepository;
    private final ConversationService conversationService;
    private final MessageService messageService;
    private final AIService aiService;
    private final WebClient.Builder webClientBuilder;
    
    /**
     * Procesar webhook entrante de WhatsApp
     */
    @Override
    @Transactional
    public void processWebhook(WhatsAppWebhookRequest request) {
        log.info("Procesando webhook de WhatsApp");
        
        if (request.getEntry() == null || request.getEntry().isEmpty()) {
            log.warn("Webhook sin entradas, ignorando");
            return;
        }
        
        // Iterar sobre las entradas del webhook
        for (WhatsAppWebhookRequest.Entry entry : request.getEntry()) {
            if (entry.getChanges() == null || entry.getChanges().isEmpty()) {
                continue;
            }
            
            for (WhatsAppWebhookRequest.Change change : entry.getChanges()) {
                processChange(change);
            }
        }
    }
    
    /**
     * Procesar un cambio del webhook
     */
    private void processChange(WhatsAppWebhookRequest.Change change) {
        WhatsAppWebhookRequest.Value value = change.getValue();
        
        if (value == null || value.getMessages() == null || value.getMessages().isEmpty()) {
            log.debug("No hay mensajes en este cambio");
            return;
        }
        
        // Procesar cada mensaje
        for (WhatsAppWebhookRequest.IncomingMessage incomingMessage : value.getMessages()) {
            processIncomingMessage(incomingMessage, value);
        }
    }
    
    /**
     * Procesar mensaje entrante
     */
    private void processIncomingMessage(
            WhatsAppWebhookRequest.IncomingMessage incomingMessage,
            WhatsAppWebhookRequest.Value value) {
        
        try {
            String phoneNumber = incomingMessage.getFrom();
            String messageId = incomingMessage.getId();
            String messageType = incomingMessage.getType();
            
            log.info("Procesando mensaje entrante de: {}, tipo: {}", phoneNumber, messageType);
            
            // Obtener contenido del mensaje
            String messageContent = extractMessageContent(incomingMessage);
            
            if (messageContent == null || messageContent.trim().isEmpty()) {
                log.warn("Mensaje sin contenido, ignorando");
                return;
            }
            
            // Obtener o crear cliente
            Customer customer = getOrCreateCustomer(phoneNumber, value);
            
            // Obtener o crear conversación activa
            Conversation conversation = conversationService.getOrCreateActiveConversation(customer);
            
            // Guardar mensaje entrante
            Message inboundMessage = messageService.createInboundMessage(
                    messageId,
                    messageContent,
                    phoneNumber,
                    conversation
            );
            
            log.info("Mensaje entrante guardado con ID: {}", inboundMessage.getId());
            
            // Generar respuesta con IA
            String aiResponse = generateAIResponse(messageContent);
            
            // Enviar respuesta
            WhatsAppMessageResponse whatsAppResponse = sendTextMessage(phoneNumber, aiResponse);
            
            // Guardar mensaje saliente
            Message outboundMessage = messageService.createOutboundMessage(
                    aiResponse,
                    phoneNumber,
                    conversation
            );
            
            // Actualizar WhatsApp Message ID si la respuesta fue exitosa
            if (whatsAppResponse != null && 
                whatsAppResponse.getMessages() != null && 
                !whatsAppResponse.getMessages().isEmpty()) {
                
                String whatsappMsgId = whatsAppResponse.getMessages().get(0).getId();
                messageService.updateWhatsAppMessageId(outboundMessage.getId(), whatsappMsgId);
            }
            
            log.info("Mensaje procesado y respuesta enviada exitosamente");
            
        } catch (Exception e) {
            log.error("Error procesando mensaje entrante: {}", e.getMessage(), e);
            throw new WhatsAppException("Error procesando mensaje de WhatsApp", e);
        }
    }
    
    /**
     * Extraer contenido del mensaje según su tipo
     */
    private String extractMessageContent(WhatsAppWebhookRequest.IncomingMessage message) {
        String type = message.getType();
        
        if ("text".equals(type) && message.getText() != null) {
            return message.getText().getBody();
        }
        
        // Para otros tipos (image, document, etc.) podrías retornar un mensaje descriptivo
        if ("image".equals(type)) {
            return "[Imagen recibida]";
        }
        
        if ("document".equals(type)) {
            return "[Documento recibido]";
        }
        
        if ("audio".equals(type)) {
            return "[Audio recibido]";
        }
        
        if ("video".equals(type)) {
            return "[Video recibido]";
        }
        
        return null;
    }
    
    /**
     * Obtener o crear cliente
     */
    private Customer getOrCreateCustomer(String phoneNumber, WhatsAppWebhookRequest.Value value) {
        log.info("Obteniendo o creando cliente para teléfono: {}", phoneNumber);
        
        return customerRepository.findByPhoneNumber(phoneNumber)
                .orElseGet(() -> {
                    log.info("Cliente no existe, creando nuevo");
                    
                    String profileName = null;
                    if (value.getContacts() != null && !value.getContacts().isEmpty()) {
                        WhatsAppWebhookRequest.Contact contact = value.getContacts().get(0);
                        if (contact.getProfile() != null) {
                            profileName = contact.getProfile().getName();
                        }
                    }
                    
                    Customer newCustomer = Customer.builder()
                            .phoneNumber(phoneNumber)
                            .profileName(profileName)
                            .lastInteraction(LocalDateTime.now())
                            .build();
                    
                    return customerRepository.save(newCustomer);
                });
    }
    
    /**
     * Generar respuesta con IA
     */
    private String generateAIResponse(String userMessage) {
        log.info("Generando respuesta de IA para mensaje: {}", userMessage);
        
        try {
            // Buscar productos relevantes
            String productsContext = aiService.searchRelevantProducts(userMessage);
            
            // Generar respuesta con contexto de productos
            var aiResponse = aiService.generateResponseWithProducts(userMessage, productsContext);
            
            return aiResponse.getContent();
            
        } catch (Exception e) {
            log.error("Error generando respuesta de IA: {}", e.getMessage(), e);
            return "Disculpa, estoy teniendo problemas para procesar tu mensaje. ¿Podrías intentarlo de nuevo?";
        }
    }
    
    /**
     * Enviar mensaje de texto a WhatsApp
     */
    @Override
    public WhatsAppMessageResponse sendTextMessage(String phoneNumber, String message) {
        log.info("Enviando mensaje de texto a: {}", phoneNumber);
        
        try {
            // Construir URL de la API
            String url = String.format("%s/%s/messages",
                    whatsAppConfig.getApi().getBase().getUrl(),
                    whatsAppConfig.getPhone().getNumber().getId());
            
            // Construir body del request
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("messaging_product", "whatsapp");
            requestBody.put("to", phoneNumber);
            requestBody.put("type", "text");
            
            Map<String, String> textObject = new HashMap<>();
            textObject.put("body", message);
            requestBody.put("text", textObject);
            
            // Enviar request usando WebClient
            WebClient webClient = webClientBuilder.build();
            
            WhatsAppMessageResponse response = webClient.post()
                    .uri(url)
                    .header("Authorization", "Bearer " + whatsAppConfig.getAccessToken())
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(WhatsAppMessageResponse.class)
                    .onErrorResume(e -> {
                        log.error("Error llamando a WhatsApp API: {}", e.getMessage(), e);
                        return Mono.empty();
                    })
                    .block();
            
            log.info("Mensaje enviado exitosamente a WhatsApp");
            
            return response;
            
        } catch (Exception e) {
            log.error("Error enviando mensaje a WhatsApp: {}", e.getMessage(), e);
            throw new WhatsAppException("Error enviando mensaje a WhatsApp", e);
        }
    }
    
    /**
     * Enviar mensaje manual (desde API REST)
     */
    @Override
    public WhatsAppMessageResponse sendMessage(MessageRequest request) {
        log.info("Enviando mensaje manual a: {}", request.getPhoneNumber());
        
        return sendTextMessage(request.getPhoneNumber(), request.getContent());
    }
    
    /**
     * Verificar webhook (GET request de WhatsApp)
     */
    @Override
    public String verifyWebhook(String mode, String token, String challenge) {
        log.info("Verificando webhook con mode: {}, token: {}", mode, token);
        
        String expectedToken = whatsAppConfig.getWebhook().getVerify().getToken();
        
        if ("subscribe".equals(mode) && expectedToken.equals(token)) {
            log.info("Webhook verificado exitosamente");
            return challenge;
        }
        
        log.warn("Verificación de webhook fallida");
        return null;
    }
}
