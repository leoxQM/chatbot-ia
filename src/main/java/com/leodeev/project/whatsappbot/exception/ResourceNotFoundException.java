package com.leodeev.project.whatsappbot.exception;

/**
 * Excepción para cuando un recurso no es encontrado (404)
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
