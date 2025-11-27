package com.leodeev.project.whatsappbot.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para enviar mensajes manualmente
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {

    @NotBlank(message = "El número de teléfono es obligatorio")
    private String phoneNumber;

    @NotBlank(message = "El contenido del mensaje es obligatorio")
    private String content;

    private String messageType; // text, image, document, etc.
}
