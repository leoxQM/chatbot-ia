package com.leodeev.project.whatsappbot.util;

import com.leodeev.project.whatsappbot.dto.response.ProductResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Constructor de prompts para IA
 * SOLID: Single Responsibility Principle - Solo construye prompts
 */
@Slf4j
public class AIPromptBuilder {
    
    /**
     * Constructor privado para evitar instanciación
     */
    private AIPromptBuilder() {
        throw new IllegalStateException("Utility class");
    }
    
    /**
     * Construir prompt del sistema para asistente de ventas
     * 
     * @param businessName Nombre del negocio
     * @param products Lista de productos
     * @return System prompt completo
     */
    public static String buildSalesAssistantPrompt(String businessName, List<ProductResponse> products) {
        log.debug("Construyendo prompt para asistente de ventas");
        
        StringBuilder prompt = new StringBuilder();
        
        // Rol y contexto
        prompt.append("Eres un asistente virtual de ventas para ").append(businessName).append(". ");
        prompt.append("Tu objetivo es ayudar a los clientes a encontrar productos que necesitan y responder sus preguntas de forma amable y profesional.\n\n");
        
        // Productos disponibles
        if (products != null && !products.isEmpty()) {
            prompt.append("PRODUCTOS DISPONIBLES:\n\n");
            
            for (ProductResponse product : products) {
                prompt.append("📦 ").append(product.getName()).append("\n");
                prompt.append("   💰 Precio: S/ ").append(product.getPrice()).append("\n");
                
                if (product.getDescription() != null && !product.getDescription().isEmpty()) {
                    prompt.append("   📝 ").append(product.getDescription()).append("\n");
                }
                
                if (product.getCategory() != null) {
                    prompt.append("   🏷️ Categoría: ").append(product.getCategory()).append("\n");
                }
                
                prompt.append("   📊 Stock: ").append(product.getStock()).append(" unidades\n\n");
            }
        } else {
            prompt.append("No hay productos disponibles en este momento.\n\n");
        }
        
        // Instrucciones de comportamiento
        prompt.append("INSTRUCCIONES:\n");
        prompt.append("1. Sé amable, profesional y servicial en todo momento\n");
        prompt.append("2. Ayuda a los clientes a encontrar productos según sus necesidades\n");
        prompt.append("3. Proporciona información clara sobre precios, características y disponibilidad\n");
        prompt.append("4. Si un producto no está disponible, sugiere alternativas similares\n");
        prompt.append("5. Mantén las respuestas concisas (máximo 3-4 párrafos)\n");
        prompt.append("6. Si no sabes algo, sé honesto y ofrece contactar con soporte humano\n");
        prompt.append("7. Usa emojis ocasionalmente para hacer la conversación más amigable\n");
        prompt.append("8. Responde SIEMPRE en español\n");
        prompt.append("9. Si el cliente pregunta por productos que no están en la lista, indícale amablemente que no los tienes disponibles\n");
        prompt.append("10. Puedes hacer preguntas de seguimiento para entender mejor las necesidades del cliente\n");
        
        return prompt.toString();
    }
    
    /**
     * Construir prompt para búsqueda de productos relevantes
     * 
     * @param userQuery Consulta del usuario
     * @return Prompt para IA
     */
    public static String buildProductSearchPrompt(String userQuery) {
        log.debug("Construyendo prompt de búsqueda para: {}", userQuery);
        
        return "Analiza la siguiente consulta del cliente y determina qué tipo de productos está buscando:\n\n" +
               "Consulta: \"" + userQuery + "\"\n\n" +
               "Responde con las categorías de productos más relevantes.";
    }
    
    /**
     * Construir prompt para generar respuesta de saludo
     * 
     * @param businessName Nombre del negocio
     * @param customerName Nombre del cliente (opcional)
     * @return Prompt de saludo
     */
    public static String buildGreetingPrompt(String businessName, String customerName) {
        log.debug("Construyendo prompt de saludo");
        
        StringBuilder prompt = new StringBuilder();
        prompt.append("Genera un saludo breve y amigable para un cliente ");
        
        if (customerName != null && !customerName.isEmpty()) {
            prompt.append("llamado ").append(customerName).append(" ");
        }
        
        prompt.append("que está contactando a ").append(businessName).append(". ");
        prompt.append("El saludo debe ser cálido, profesional y ofrecer ayuda. ");
        prompt.append("Máximo 2 líneas.");
        
        return prompt.toString();
    }
    
    /**
     * Construir prompt para despedida
     * 
     * @return Prompt de despedida
     */
    public static String buildFarewellPrompt() {
        log.debug("Construyendo prompt de despedida");
        
        return "Genera una despedida breve y profesional para un cliente que termina la conversación. " +
               "Agradece su contacto e invítalo a volver. Máximo 2 líneas.";
    }
    
    /**
     * Construir contexto de conversación para IA
     * 
     * @param messages Lista de mensajes previos
     * @return Contexto formateado
     */
    public static String buildConversationContext(List<String> messages) {
        if (messages == null || messages.isEmpty()) {
            return "";
        }
        
        log.debug("Construyendo contexto de conversación con {} mensajes", messages.size());
        
        StringBuilder context = new StringBuilder();
        context.append("HISTORIAL DE CONVERSACIÓN:\n\n");
        
        for (int i = 0; i < messages.size(); i++) {
            String role = i % 2 == 0 ? "Cliente" : "Asistente";
            context.append(role).append(": ").append(messages.get(i)).append("\n");
        }
        
        return context.toString();
    }
}
