package com.nml.core.application.mapper;
import com.nml.core.application.dto.product.ProductRequest;
import com.nml.core.application.dto.product.ProductResponse;
import com.nml.core.application.mapper.ProductMapper;
import com.nml.core.domain.model.Category;
import com.nml.core.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
public class ProductMapperUnitTests {

    Product product;
    Product product2;
    Category category;

    ProductRequest productRequest;

    HashSet<Product> products = new HashSet<>();

    @BeforeEach
    public void setup(){
        category = Category.builder()
                .id(UUID.randomUUID())
                .description("Category 1 Description")
                .name("Category 1")
                .deleted(false)
                .build();

        HashSet<Category> categories = new HashSet<>();
        categories.add(category);

        product = Product.builder()
                .id(UUID.randomUUID())
                .description("Product 1 Description")
                .name("Product 1")
                .imagelink("Image Link")
                .quantity(10)
                .details("Details")
                .price(100)
                .deleted(false)
                .categories(categories)
                .build();

        product2 = Product.builder()
                .id(UUID.randomUUID())
                .description("Product 2 Description")
                .name("Product 2")
                .imagelink("Image Link")
                .quantity(5)
                .details("Details")
                .price(100)
                .deleted(false)
                .categories(categories)
                .build();

        products.add(product);
        products.add(product2);

        productRequest = ProductRequest.builder()
                .id(UUID.randomUUID())
                .description("Product Request Description")
                .deleted(false)
                .name("Product Request Name")
                .details("Product Request Details")
                .description("Product Request Description")
                .price(1)
                .imagelink("Product Request Image link")
                .quantity(10)
                .build();

}

    @Test
    void mapProductToProductResponseGoodTest() {
        ProductResponse productResponse = ProductMapper.mapProductToProductResponse(product);

        assertEquals(product.getId(), productResponse.getId());
        assertEquals(product.getCategories(), productResponse.getCategories()); //
        assertEquals(product.getDescription(), productResponse.getDescription());
        assertEquals(product.getDetails(), productResponse.getDetails());
        assertEquals(product.getPrice(), productResponse.getPrice());
        assertEquals(product.getImagelink(), productResponse.getImagelink());
        assertEquals(product.getName(), productResponse.getName());
        assertEquals(product.isDeleted(), productResponse.isDeleted());
        assertEquals(product.getQuantity(), productResponse.getQuantity());

    }

    @Test
    void mapProductRequestToProductGoodTest() {
        Product mappedProduct = ProductMapper.mapProductRequestToProduct(productRequest);

        assertEquals(productRequest.getId(), mappedProduct.getId());
        assertEquals(productRequest.getCategories(), mappedProduct.getCategories()); //
        assertEquals(productRequest.getDescription(), mappedProduct.getDescription());
        assertEquals(productRequest.getDetails(), mappedProduct.getDetails());
        assertEquals(productRequest.getPrice(), mappedProduct.getPrice());
        assertEquals(productRequest.getImagelink(), mappedProduct.getImagelink());
        assertEquals(productRequest.getName(), mappedProduct.getName());
        assertEquals(productRequest.isDeleted(), mappedProduct.isDeleted());
        assertEquals(productRequest.getQuantity(), mappedProduct.getQuantity());
    }

    @Test
    void mapProductsToProductResponsesGoodTest(){
        List<ProductResponse> productResponses = ProductMapper.mapProductsToProductResponses(products);

        assertTrue((productResponses).size() == 2);
        assertTrue((productResponses).contains(ProductMapper.mapProductToProductResponse(product)));
        assertTrue((productResponses).contains(ProductMapper.mapProductToProductResponse(product2)));
    }

}
