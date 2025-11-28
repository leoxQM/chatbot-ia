package com.leodeev.project.whatsappbot.service.serviceImpl;

import com.leodeev.project.whatsappbot.config.AIConfig;
import com.leodeev.project.whatsappbot.dto.response.AIResponse;
import com.leodeev.project.whatsappbot.dto.response.ProductResponse;
import com.leodeev.project.whatsappbot.exception.AIServiceException;
import com.leodeev.project.whatsappbot.service.AIService;
import com.leodeev.project.whatsappbot.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementación del servicio de IA usando OpenAI (GPT-3.5-turbo)
 * SOLID: Single Responsibility Principle - Solo maneja lógica de IA
 * SOLID: Open/Closed Principle - Se puede extender para otros proveedores
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {
    
    private final AIConfig aiConfig;
    private final ProductService productService;
    private final WebClient.Builder webClientBuilder;
    
    /**
     * Generar respuesta de IA basada en el mensaje del usuario
     */
    @Override
    public AIResponse generateResponse(String userMessage, List<String> conversationHistory) {
        log.info("Generando respuesta de IA para mensaje: {}", userMessage);
        
        try {
            // Construir mensajes de conversación
            List<Map<String, String>> messages = new ArrayList<>();
            
            // Agregar historial si existe
            if (conversationHistory != null && !conversationHistory.isEmpty()) {
                for (int i = 0; i < conversationHistory.size(); i++) {
                    String role = i % 2 == 0 ? "user" : "assistant";
                    Map<String, String> msg = new HashMap<>();
                    msg.put("role", role);
                    msg.put("content", conversationHistory.get(i));
                    messages.add(msg);
                }
            }
            
            // Agregar mensaje actual
            Map<String, String> currentMessage = new HashMap<>();
            currentMessage.put("role", "user");
            currentMessage.put("content", userMessage);
            messages.add(currentMessage);
            
            // Llamar a OpenAI API
            return callOpenAIAPI(messages, null);
            
        } catch (Exception e) {
            log.error("Error generando respuesta de IA: {}", e.getMessage(), e);
            throw new AIServiceException("Error al generar respuesta de IA", e);
        }
    }
    
    /**
     * Generar respuesta de IA con contexto de productos
     */
    @Override
    public AIResponse generateResponseWithProducts(String userMessage, String productsContext) {
        log.info("Generando respuesta de IA con contexto de productos");
        
        try {
            // Construir prompt del sistema con contexto de productos
            String systemPrompt = buildSystemPromptWithProducts(productsContext);
            
            // Construir mensaje del usuario
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", userMessage);
            messages.add(userMsg);
            
            // Llamar a OpenAI API con system prompt
            return callOpenAIAPI(messages, systemPrompt);
            
        } catch (Exception e) {
            log.error("Error generando respuesta de IA con productos: {}", e.getMessage(), e);
            throw new AIServiceException("Error al generar respuesta de IA", e);
        }
    }
    
    /**
     * Buscar productos relevantes según la consulta del usuario
     */
    @Override
    public String searchRelevantProducts(String userQuery) {
        log.info("Buscando productos relevantes para: {}", userQuery);
        
        try {
            // Obtener todos los productos activos
            List<ProductResponse> products = productService.getActiveProducts();
            
            if (products.isEmpty()) {
                return "No hay productos disponibles en este momento.";
            }
            
            // Construir contexto de productos
            StringBuilder context = new StringBuilder();
            context.append("Productos disponibles:\n\n");
            
            for (ProductResponse product : products) {
                context.append("- ").append(product.getName()).append("\n");
                context.append("  Precio: S/ ").append(product.getPrice()).append("\n");
                
                if (product.getDescription() != null && !product.getDescription().isEmpty()) {
                    context.append("  Descripción: ").append(product.getDescription()).append("\n");
                }
                
                if (product.getCategory() != null && !product.getCategory().isEmpty()) {
                    context.append("  Categoría: ").append(product.getCategory()).append("\n");
                }
                
                context.append("  Stock disponible: ").append(product.getStock()).append(" unidades\n\n");
            }
            
            return context.toString();
            
        } catch (Exception e) {
            log.error("Error buscando productos: {}", e.getMessage(), e);
            return "Error al buscar productos.";
        }
    }
    
    /**
     * Construir system prompt con contexto de productos
     */
    private String buildSystemPromptWithProducts(String productsContext) {
        return "Eres un asistente virtual de ventas para Leo Imports. " +
               "Tu objetivo es ayudar a los clientes a encontrar productos que necesitan y responder sus preguntas.\n\n" +
               "Información de productos:\n" +
               productsContext + "\n\n" +
               "Instrucciones:\n" +
               "1. Sé amable y profesional en todo momento\n" +
               "2. Ayuda a los clientes a encontrar productos según sus necesidades\n" +
               "3. Proporciona información clara sobre precios y disponibilidad\n" +
               "4. Si un producto no está disponible, sugiere alternativas\n" +
               "5. Mantén las respuestas concisas y útiles\n" +
               "6. Si no sabes algo, sé honesto y ofrece ayuda alternativa\n" +
               "7. Responde siempre en español\n" +
               "8. Si el cliente pregunta por productos que no están en la lista, indícale que no los tienes disponibles actualmente";
    }
    
    /**
     * Llamar a OpenAI API (GPT-3.5-turbo)
     */
    private AIResponse callOpenAIAPI(List<Map<String, String>> messages, String systemPrompt) {
        log.info("Llamando a OpenAI API");
        
        try {
            WebClient webClient = webClientBuilder.build();
            
            // Agregar system prompt si existe
            List<Map<String, String>> messagesWithSystem = new ArrayList<>();
            if (systemPrompt != null && !systemPrompt.isEmpty()) {
                Map<String, String> systemMessage = new HashMap<>();
                systemMessage.put("role", "system");
                systemMessage.put("content", systemPrompt);
                messagesWithSystem.add(systemMessage);
            }
            messagesWithSystem.addAll(messages);
            
            // Construir request body para OpenAI
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-4o-mini");
            requestBody.put("max_tokens", aiConfig.getClaude().getMax().getTokens());
            requestBody.put("messages", messagesWithSystem);
            requestBody.put("temperature", 0.7);
            
            // Hacer llamada a OpenAI API
            String openaiApiKey = System.getenv("OPENAI_API_KEY");
            if (openaiApiKey == null || openaiApiKey.isEmpty()) {
                throw new AIServiceException("OPENAI_API_KEY no configurada");
            }
            
            @SuppressWarnings("unchecked")
            Map<String, Object> response = (Map<String, Object>) webClient.post()
                    .uri("https://api.openai.com/v1/chat/completions")
                    .header("Authorization", "Bearer " + openaiApiKey)
                    .header("content-type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .onErrorResume(e -> {
                        log.error("Error en llamada a OpenAI API: {}", e.getMessage(), e);
                        return Mono.empty();
                    })
                    .block();
            
            if (response == null) {
                throw new AIServiceException("Respuesta vacía de OpenAI API");
            }
            
            // Extraer contenido de la respuesta
            String content = extractContentFromOpenAIResponse(response);
            
            // Extraer información adicional
            String model = (String) response.get("model");
            @SuppressWarnings("unchecked")
            Map<String, Object> usage = (Map<String, Object>) response.get("usage");
            Integer tokensUsed = usage != null ? (Integer) usage.get("completion_tokens") : null;
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            String finishReason = choices != null && !choices.isEmpty() ? 
                    (String) choices.get(0).get("finish_reason") : null;
            
            log.info("Respuesta de IA generada exitosamente");
            
            return AIResponse.builder()
                    .content(content)
                    .model(model)
                    .tokensUsed(tokensUsed)
                    .finishReason(finishReason)
                    .build();
            
        } catch (Exception e) {
            log.error("Error llamando a OpenAI API: {}", e.getMessage(), e);
            throw new AIServiceException("Error al llamar a OpenAI API", e);
        }
    }
    
    /**
     * Extraer contenido de la respuesta de OpenAI
     */
    @SuppressWarnings("unchecked")
    private String extractContentFromOpenAIResponse(Map<String, Object> response) {
        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> choice = choices.get(0);
                Map<String, Object> message = (Map<String, Object>) choice.get("message");
                if (message != null) {
                    return (String) message.get("content");
                }
            }
            
            return "No se pudo generar una respuesta.";
            
        } catch (Exception e) {
            log.error("Error extrayendo contenido de respuesta de OpenAI: {}", e.getMessage(), e);
            return "Error procesando respuesta.";
        }
    }
}