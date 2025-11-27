package com.leodeev.project.whatsappbot.mapper;

import com.leodeev.project.whatsappbot.dto.response.ConversationResponse;
import com.leodeev.project.whatsappbot.entity.Conversation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre Conversation Entity y DTOs
 */
@Component
public class ConversationMapper {

    /**
     * Convierte Conversation Entity a ConversationResponse
     * @param conversation Entity
     * @return ConversationResponse DTO
     */
    public ConversationResponse toResponse(Conversation conversation) {
        if (conversation == null) {
            return null;
        }

        return ConversationResponse.builder()
                .id(conversation.getId())
                .customerId(conversation.getCustomer() != null ? conversation.getCustomer().getId() : null)
                .customerPhoneNumber(conversation.getCustomer() != null ? conversation.getCustomer().getPhoneNumber() : null)
                .customerName(conversation.getCustomer() != null ? conversation.getCustomer().getName() : null)
                .startedAt(conversation.getStartedAt())
                .endedAt(conversation.getEndedAt())
                .status(conversation.getStatus() != null ? conversation.getStatus().name() : null)
                .topic(conversation.getTopic())
                .messageCount(conversation.getMessages() != null ? conversation.getMessages().size() : 0)
                .createdAt(conversation.getCreatedAt())
                .updatedAt(conversation.getUpdatedAt())
                .build();
    }

    /**
     * Convierte lista de Conversation a lista de ConversationResponse
     * @param conversations Lista de entities
     * @return Lista de DTOs
     */
    public List<ConversationResponse> toResponseList(List<Conversation> conversations) {
        if (conversations == null) {
            return null;
        }

        return conversations.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
