package com.leodeev.project.whatsappbot.controller;

import com.leodeev.project.whatsappbot.dto.request.ProductRequest;
import com.leodeev.project.whatsappbot.dto.response.ProductResponse;
import com.leodeev.project.whatsappbot.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de Productos
 * SOLID: Single Responsibility Principle - Solo maneja endpoints de productos
 * SOLID: Dependency Inversion - Depende de ProductService (interface)
 */
@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    
    /**
     * Crear un nuevo producto
     * POST /api/products
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        log.info("Solicitud para crear producto: {}", request.getName());
        
        ProductResponse response = productService.createProduct(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Obtener producto por ID
     * GET /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        log.info("Solicitud para obtener producto con ID: {}", id);
        
        ProductResponse response = productService.getProductById(id);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener todos los productos
     * GET /api/products
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        log.info("Solicitud para obtener todos los productos");
        
        List<ProductResponse> response = productService.getAllProducts();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener productos activos
     * GET /api/products/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<ProductResponse>> getActiveProducts() {
        log.info("Solicitud para obtener productos activos");
        
        List<ProductResponse> response = productService.getActiveProducts();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Actualizar producto
     * PUT /api/products/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        
        log.info("Solicitud para actualizar producto con ID: {}", id);
        
        ProductResponse response = productService.updateProduct(id, request);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Eliminar producto
     * DELETE /api/products/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("Solicitud para eliminar producto con ID: {}", id);
        
        productService.deleteProduct(id);
        
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Buscar productos por nombre
     * GET /api/products/search?name=laptop
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String name) {
        log.info("Solicitud para buscar productos con nombre: {}", name);
        
        List<ProductResponse> response = productService.searchProductsByName(name);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener productos por categoría
     * GET /api/products/category/{category}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable String category) {
        log.info("Solicitud para obtener productos de categoría: {}", category);
        
        List<ProductResponse> response = productService.getProductsByCategory(category);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener todas las categorías
     * GET /api/products/categories
     */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        log.info("Solicitud para obtener todas las categorías");
        
        List<String> response = productService.getAllCategories();
        
        return ResponseEntity.ok(response);
    }
}
