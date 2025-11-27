package com.leodeev.project.whatsappbot.service;

import com.leodeev.project.whatsappbot.dto.request.ProductRequest;
import com.leodeev.project.whatsappbot.dto.response.ProductResponse;

import java.util.List;

/**
 * Interfaz del servicio de Productos
 * SOLID: Dependency Inversion Principle (DIP)
 */
public interface ProductService {

    /**
     * Crear un nuevo producto
     * @param request Datos del producto
     * @return Producto creado
     */
    ProductResponse createProduct(ProductRequest request);

    /**
     * Obtener un producto por ID
     * @param id ID del producto
     * @return Producto encontrado
     */
    ProductResponse getProductById(Long id);

    /**
     * Obtener todos los productos
     * @return Lista de productos
     */
    List<ProductResponse> getAllProducts();

    /**
     * Obtener productos activos
     * @return Lista de productos activos
     */
    List<ProductResponse> getActiveProducts();

    /**
     * Actualizar un producto
     * @param id ID del producto
     * @param request Nuevos datos
     * @return Producto actualizado
     */
    ProductResponse updateProduct(Long id, ProductRequest request);

    /**
     * Eliminar un producto
     * @param id ID del producto
     */
    void deleteProduct(Long id);

    /**
     * Buscar productos por nombre
     * @param name Nombre a buscar
     * @return Lista de productos encontrados
     */
    List<ProductResponse> searchProductsByName(String name);

    /**
     * Buscar productos por categoría
     * @param category Categoría
     * @return Lista de productos
     */
    List<ProductResponse> getProductsByCategory(String category);

    /**
     * Obtener todas las categorías
     * @return Lista de categorías
     */
    List<String> getAllCategories();
}
