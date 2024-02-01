package com.nml.core.application.mapper;

import com.nml.core.application.dto.product.ProductRequest;
import com.nml.core.application.dto.product.ProductResponse;
import com.nml.core.domain.model.Category;
import com.nml.core.domain.model.Product;

import java.util.*;

public class ProductMapper {

    public static ProductResponse mapProductToProductResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();

        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setPrice(product.getPrice());
        productResponse.setQuantity(product.getQuantity());
        productResponse.setCategories(product.getCategories());
        productResponse.setDeleted(product.isDeleted());
        productResponse.setDescription(product.getDescription());
        productResponse.setDetails(product.getDetails());
        productResponse.setImagelink(product.getImagelink());

        return productResponse;
    }

    public static Product mapProductRequestToProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .id(productRequest.getId())
                .deleted(productRequest.isDeleted())
                .description(productRequest.getDescription())
                .details(productRequest.getDetails())
                .imagelink(productRequest.getImagelink())
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .build();

        if(productRequest.getCategories()!= null && productRequest.getCategories().isEmpty() == false) {
            for(Category category : productRequest.getCategories()) {
                product.addCategory(category);
            }
        }

        return product;
    }

    public static List<ProductResponse> mapProductsToProductResponses(Iterable<Product> products) {
        List<ProductResponse> productResponses = new ArrayList<>();

        for (Product product : products) {
            productResponses.add(mapProductToProductResponse(product));
        }

        return productResponses;
    }

}
