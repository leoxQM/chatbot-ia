package com.leodeev.project.whatsappbot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para conversaciones
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponse {

    private Long id;
    private Long customerId;
    private String customerPhoneNumber;
    private String customerName;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private String status;
    private String topic;
    private Integer messageCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
