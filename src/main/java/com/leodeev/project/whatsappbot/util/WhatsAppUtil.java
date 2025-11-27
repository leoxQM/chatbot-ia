package com.leodeev.project.whatsappbot.util;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

/**
 * Utilidades para WhatsApp
 * SOLID: Single Responsibility Principle - Solo funciones auxiliares de WhatsApp
 */
@Slf4j
public class WhatsAppUtil {
    
    // Patrón para validar números de teléfono (formato internacional)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10,15}$");
    
    /**
     * Constructor privado para evitar instanciación
     */
    private WhatsAppUtil() {
        throw new IllegalStateException("Utility class");
    }
    
    /**
     * Formatear número de teléfono para WhatsApp
     * Elimina caracteres especiales y espacios
     * 
     * @param phoneNumber Número de teléfono
     * @return Número formateado (solo dígitos)
     */
    public static String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return phoneNumber;
        }
        
        // Eliminar espacios, guiones, paréntesis, signos +
        String formatted = phoneNumber.replaceAll("[\\s\\-()\\+]", "");
        
        log.debug("Número formateado de '{}' a '{}'", phoneNumber, formatted);
        
        return formatted;
    }
    
    /**
     * Validar formato de número de teléfono
     * 
     * @param phoneNumber Número a validar
     * @return true si es válido, false si no
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }
        
        String formatted = formatPhoneNumber(phoneNumber);
        boolean isValid = PHONE_PATTERN.matcher(formatted).matches();
        
        log.debug("Validación de número '{}': {}", phoneNumber, isValid);
        
        return isValid;
    }
    
    /**
     * Agregar código de país si no lo tiene
     * 
     * @param phoneNumber Número de teléfono
     * @param countryCode Código de país (ej: "51" para Perú)
     * @return Número con código de país
     */
    public static String addCountryCode(String phoneNumber, String countryCode) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return phoneNumber;
        }
        
        String formatted = formatPhoneNumber(phoneNumber);
        
        // Si ya empieza con el código de país, no agregar
        if (formatted.startsWith(countryCode)) {
            return formatted;
        }
        
        // Agregar código de país
        String withCountryCode = countryCode + formatted;
        
        log.debug("Número con código de país: '{}'", withCountryCode);
        
        return withCountryCode;
    }
    
    /**
     * Extraer el cuerpo del mensaje de texto
     * Maneja diferentes formatos y espacios
     * 
     * @param message Mensaje
     * @return Mensaje limpio
     */
    public static String cleanMessage(String message) {
        if (message == null) {
            return "";
        }
        
        // Eliminar espacios al inicio y final
        String cleaned = message.trim();
        
        // Eliminar múltiples espacios consecutivos
        cleaned = cleaned.replaceAll("\\s+", " ");
        
        return cleaned;
    }
    
    /**
     * Truncar mensaje si excede el límite de WhatsApp
     * WhatsApp tiene límite de 4096 caracteres por mensaje
     * 
     * @param message Mensaje
     * @return Mensaje truncado si es necesario
     */
    public static String truncateMessage(String message) {
        if (message == null) {
            return "";
        }
        
        final int MAX_LENGTH = 4096;
        
        if (message.length() <= MAX_LENGTH) {
            return message;
        }
        
        log.warn("Mensaje truncado de {} a {} caracteres", message.length(), MAX_LENGTH);
        
        return message.substring(0, MAX_LENGTH - 3) + "...";
    }
    
    /**
     * Verificar si un mensaje es un comando
     * Comandos empiezan con / (ej: /help, /start)
     * 
     * @param message Mensaje
     * @return true si es comando
     */
    public static boolean isCommand(String message) {
        if (message == null || message.isEmpty()) {
            return false;
        }
        
        return message.trim().startsWith("/");
    }
    
    /**
     * Extraer comando del mensaje
     * 
     * @param message Mensaje
     * @return Comando sin el slash
     */
    public static String extractCommand(String message) {
        if (!isCommand(message)) {
            return "";
        }
        
        String cleaned = message.trim();
        
        // Remover el slash inicial
        String command = cleaned.substring(1);
        
        // Si tiene espacios, tomar solo la primera palabra
        int spaceIndex = command.indexOf(' ');
        if (spaceIndex > 0) {
            command = command.substring(0, spaceIndex);
        }
        
        return command.toLowerCase();
    }
}
