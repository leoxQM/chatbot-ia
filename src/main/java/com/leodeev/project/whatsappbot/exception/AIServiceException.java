package com.leodeev.project.whatsappbot.exception;

/**
 * Excepción para errores relacionados con servicios de IA
 */
public class AIServiceException extends RuntimeException {
    
    public AIServiceException(String message) {
        super(message);
    }
    
    public AIServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
