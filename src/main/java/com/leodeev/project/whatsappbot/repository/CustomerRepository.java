package com.leodeev.project.whatsappbot.repository;

import com.leodeev.project.whatsappbot.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Customer
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    /**
     * Buscar cliente por número de teléfono
     */
    Optional<Customer> findByPhoneNumber(String phoneNumber);
    
    /**
     * Verificar si existe un cliente con ese teléfono
     */
    boolean existsByPhoneNumber(String phoneNumber);
    
    /**
     * Buscar clientes por nombre (búsqueda parcial)
     */
    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Customer> searchByName(@Param("name") String name);
    
    /**
     * Buscar clientes con interacción reciente
     */
    @Query("SELECT c FROM Customer c WHERE c.lastInteraction >= :since ORDER BY c.lastInteraction DESC")
    List<Customer> findRecentCustomers(@Param("since") LocalDateTime since);
    
    /**
     * Buscar clientes inactivos (sin interacción desde cierta fecha)
     */
    @Query("SELECT c FROM Customer c WHERE c.lastInteraction < :since OR c.lastInteraction IS NULL")
    List<Customer> findInactiveCustomers(@Param("since") LocalDateTime since);
}
