package com.leodeev.project.whatsappbot.repository;

import com.leodeev.project.whatsappbot.entity.Conversation;
import com.leodeev.project.whatsappbot.entity.Conversation.ConversationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Conversation
 */
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    
    /**
     * Buscar conversaciones por ID de cliente
     */
    List<Conversation> findByCustomerId(Long customerId);
    
    /**
     * Buscar conversaciones activas de un cliente
     */
    List<Conversation> findByCustomerIdAndStatus(Long customerId, ConversationStatus status);
    
    /**
     * Buscar la conversación activa más reciente de un cliente
     */
    @Query("SELECT c FROM Conversation c WHERE c.customer.id = :customerId AND c.status = 'ACTIVE' ORDER BY c.startedAt DESC")
    Optional<Conversation> findActiveConversationByCustomerId(@Param("customerId") Long customerId);
    
    /**
     * Buscar conversaciones por estado
     */
    List<Conversation> findByStatus(ConversationStatus status);
    
    /**
     * Buscar conversaciones en un rango de fechas
     */
    @Query("SELECT c FROM Conversation c WHERE c.startedAt BETWEEN :startDate AND :endDate")
    List<Conversation> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                       @Param("endDate") LocalDateTime endDate);
    
    /**
     * Buscar conversaciones por número de teléfono del cliente
     */
    @Query("SELECT c FROM Conversation c WHERE c.customer.phoneNumber = :phoneNumber ORDER BY c.startedAt DESC")
    List<Conversation> findByCustomerPhoneNumber(@Param("phoneNumber") String phoneNumber);
    
    /**
     * Contar conversaciones activas
     */
    long countByStatus(ConversationStatus status);
    
    /**
     * Buscar conversaciones sin cerrar (sin endedAt)
     */
    @Query("SELECT c FROM Conversation c WHERE c.endedAt IS NULL")
    List<Conversation> findOpenConversations();
}
