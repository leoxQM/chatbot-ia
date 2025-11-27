package com.leodeev.project.whatsappbot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta de la IA
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIResponse {

    private String content;
    private String model;
    private Integer tokensUsed;
    private String finishReason;
}
