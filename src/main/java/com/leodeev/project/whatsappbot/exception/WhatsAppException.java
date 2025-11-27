package com.leodeev.project.whatsappbot.exception;

/**
 * Excepción para errores relacionados con WhatsApp API
 */
public class WhatsAppException extends RuntimeException {
    
    public WhatsAppException(String message) {
        super(message);
    }
    
    public WhatsAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
