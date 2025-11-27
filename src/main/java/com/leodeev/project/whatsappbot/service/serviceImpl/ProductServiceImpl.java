package com.leodeev.project.whatsappbot.service.serviceImpl;

import com.leodeev.project.whatsappbot.dto.request.ProductRequest;
import com.leodeev.project.whatsappbot.dto.response.ProductResponse;
import com.leodeev.project.whatsappbot.entity.Product;
import com.leodeev.project.whatsappbot.exception.ResourceNotFoundException;
import com.leodeev.project.whatsappbot.mapper.ProductMapper;
import com.leodeev.project.whatsappbot.repository.ProductRepository;
import com.leodeev.project.whatsappbot.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio de Productos
 * SOLID: Single Responsibility Principle - Solo maneja lógica de productos
 * SOLID: Dependency Inversion - Depende de interfaces (Repository, Mapper)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    
    /**
     * Crear un nuevo producto
     */
    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating new product: {}", request.getName());
        
        // Convertir Request a Entity
        Product product = productMapper.toEntity(request);
        
        // Guardar en BD
        Product savedProduct = productRepository.save(product);
        
        log.info("Product created successfully with ID: {}", savedProduct.getId());
        
        // Convertir Entity a Response
        return productMapper.toResponse(savedProduct);
    }
    
    /**
     * Obtener producto por ID
     */
    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        log.info("Fetching product with ID: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        
        return productMapper.toResponse(product);
    }
    
    /**
     * Obtener todos los productos
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all products");
        
        List<Product> products = productRepository.findAll();
        
        return productMapper.toResponseList(products);
    }
    
    /**
     * Obtener productos activos
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getActiveProducts() {
        log.info("Fetching active products");
        
        List<Product> products = productRepository.findByActiveTrue();
        
        return productMapper.toResponseList(products);
    }
    
    /**
     * Actualizar producto
     */
    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        log.info("Updating product with ID: {}", id);
        
        // Buscar producto existente
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        
        // Actualizar campos
        productMapper.updateEntityFromRequest(existingProduct, request);
        
        // Guardar cambios
        Product updatedProduct = productRepository.save(existingProduct);
        
        log.info("Product updated successfully with ID: {}", updatedProduct.getId());
        
        return productMapper.toResponse(updatedProduct);
    }
    
    /**
     * Eliminar producto
     */
    @Override
    @Transactional
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        
        // Verificar que existe
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + id);
        }
        
        // Eliminar (o podrías hacer soft delete cambiando active a false)
        productRepository.deleteById(id);
        
        log.info("Product deleted successfully with ID: {}", id);
    }
    
    /**
     * Buscar productos por nombre
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProductsByName(String name) {
        log.info("Searching products by name: {}", name);
        
        List<Product> products = productRepository.searchByName(name);
        
        return productMapper.toResponseList(products);
    }
    
    /**
     * Buscar productos por categoría
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(String category) {
        log.info("Fetching products by category: {}", category);
        
        List<Product> products = productRepository.findByCategoryAndActiveTrue(category);
        
        return productMapper.toResponseList(products);
    }
    
    /**
     * Obtener todas las categorías
     */
    @Override
    @Transactional(readOnly = true)
    public List<String> getAllCategories() {
        log.info("Fetching all product categories");
        
        return productRepository.findAllCategories();
    }
}
