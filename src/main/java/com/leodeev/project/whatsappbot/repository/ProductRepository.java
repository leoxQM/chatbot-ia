package com.leodeev.project.whatsappbot.repository;

import com.leodeev.project.whatsappbot.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Product
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * Buscar productos activos
     */
    List<Product> findByActiveTrue();
    
    /**
     * Buscar productos por categoría
     */
    List<Product> findByCategoryAndActiveTrue(String category);
    
    /**
     * Buscar producto por SKU
     */
    Optional<Product> findBySku(String sku);
    
    /**
     * Buscar productos por nombre (búsqueda parcial)
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.active = true")
    List<Product> searchByName(@Param("name") String name);
    
    /**
     * Buscar productos por rango de precio
     */
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.active = true")
    List<Product> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
    
    /**
     * Buscar productos con stock disponible
     */
    @Query("SELECT p FROM Product p WHERE p.stock > 0 AND p.active = true")
    List<Product> findAvailableProducts();
    
    /**
     * Buscar todas las categorías distintas
     */
    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.active = true")
    List<String> findAllCategories();
}
