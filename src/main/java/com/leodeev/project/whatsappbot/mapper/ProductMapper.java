package com.leodeev.project.whatsappbot.mapper;

import com.leodeev.project.whatsappbot.dto.request.ProductRequest;
import com.leodeev.project.whatsappbot.dto.response.ProductResponse;
import com.leodeev.project.whatsappbot.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre Product Entity y DTOs
 */
@Component
public class ProductMapper {

    /**
     * Convierte ProductRequest a Product Entity
     * @param request DTO de request
     * @return Product entity
     */
    public Product toEntity(ProductRequest request) {
        if (request == null) {
            return null;
        }

        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .stock(request.getStock())
                .sku(request.getSku())
                .imageUrl(request.getImageUrl())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();
    }

    /**
     * Convierte Product Entity a ProductResponse
     * @param product Entity
     * @return ProductResponse DTO
     */
    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .stock(product.getStock())
                .sku(product.getSku())
                .imageUrl(product.getImageUrl())
                .active(product.getActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    /**
     * Actualiza un Product Entity existente con datos de ProductRequest
     * @param entity Entity existente
     * @param request DTO con nuevos datos
     */
    public void updateEntityFromRequest(Product entity, ProductRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setPrice(request.getPrice());
        entity.setCategory(request.getCategory());
        entity.setStock(request.getStock());
        entity.setSku(request.getSku());
        entity.setImageUrl(request.getImageUrl());

        if (request.getActive() != null) {
            entity.setActive(request.getActive());
        }
    }

    /**
     * Convierte lista de Product a lista de ProductResponse
     * @param products Lista de entities
     * @return Lista de DTOs
     */
    public List<ProductResponse> toResponseList(List<Product> products) {
        if (products == null) {
            return null;
        }

        return products.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
