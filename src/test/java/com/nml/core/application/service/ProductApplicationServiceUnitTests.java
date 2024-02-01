package com.nml.core.application.service;

import com.nml.core.application.dto.category.CategoryRequest;
import com.nml.core.application.dto.product.ProductRequest;
import com.nml.core.application.dto.product.ProductResponse;
import com.nml.core.application.mapper.ProductMapper;
import com.nml.core.application.service.exceptions.ProductApplicationServiceException;
import com.nml.core.application.service.impl.ProductApplicationService;
import com.nml.core.application.service.interfaces.IProductApplicationService;
import com.nml.core.domain.model.Category;
import com.nml.core.domain.model.Product;
import com.nml.core.domain.service.interfaces.IProductService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ProductApplicationServiceUnitTests {

    @Mock
    IProductService productService;

    @InjectMocks
    ProductApplicationService productApplicationService;

    ProductRequest productRequest, invalidProductRequest;
    UUID productId = UUID.randomUUID();

    Product product;
    Product invalidProduct;

    List<Product> productList;
    List<ProductResponse> productResponses;

    Category category;
    Category category2;

    @BeforeEach
    void setup(){
        category = Category.builder()
                .id(UUID.randomUUID())
                .name("Category 1")
                .description("Category 1 Description")
                .deleted(false)
                .build();

        category2 = Category.builder()
                .id(UUID.randomUUID())
                .name("Category 2")
                .description("Category 2 Description")
                .deleted(false)
                .build();

        HashSet<Category> categories = new HashSet<>();
        categories.add(category);
        categories.add(category2);

        productRequest = ProductRequest.builder()
                .id(productId)
                .name("Product 1")
                .description("Product 1 Description")
                .deleted(false)
                .quantity(10)
                .imagelink("Image Link")
                .details("Proudct 1 Details")
                .price(1000)
                .categories(categories)
                .build();

        product = ProductMapper.mapProductRequestToProduct(productRequest);

        // no categories
        invalidProductRequest = ProductRequest.builder()
                .id(UUID.randomUUID())
                .name("Product 1")
                .description("Product 1 Description")
                .deleted(false)
                .quantity(10)
                .imagelink("Image Link")
                .details("Product 1 Details")
                .price(1000)
                .build();

        invalidProduct = ProductMapper.mapProductRequestToProduct(invalidProductRequest);

        productList = new ArrayList<>();
        productList.add(product);

        productResponses = new ArrayList<>();
        productResponses.add(ProductMapper.mapProductToProductResponse(product));

    }

    @Test
    void createProductGoodTest(){
        when(productService.createProduct(product)).thenReturn(true);

        assertTrue(productApplicationService.createProduct(productRequest));
    }

    @Test
    void createProductBadTest(){
        when(productService.createProduct(invalidProduct)).thenReturn(false);

        assertFalse(productApplicationService.createProduct(invalidProductRequest));
    }

    @Test
    void updateProductGoodTest() {
        when(productService.updateProduct(product)).thenReturn(true);

        assertTrue(productApplicationService.updateProduct(productRequest));
    }

    @Test
    void updateProductBadTest() {
        when(productService.updateProduct(invalidProduct)).thenReturn(false);

        assertFalse(productApplicationService.updateProduct(invalidProductRequest));
    }

    @Test
    void updateProductExceptionTest() {
        when(productService.updateProduct(invalidProduct)).thenThrow(new ProductApplicationServiceException("Error while updating product"));

        Throwable exception = assertThrows(ProductApplicationServiceException.class, () -> {
            productApplicationService.updateProduct(invalidProductRequest);
        });

        assertEquals("Error while updating product", exception.getMessage());
    }

    @Test
    void deleteProductGoodTest() {
        when(productService.deleteProduct(productId)).thenReturn(true);

        assertTrue(productApplicationService.deleteProduct(productId));
    }

    @Test
    void deleteProductBadTest() {
        UUID immagineId = UUID.randomUUID();
        when(productService.deleteProduct(immagineId)).thenReturn(false);

        assertFalse(productApplicationService.deleteProduct(immagineId));
    }

    @Test
    void deleteProductExceptionTest() {
        when(productService.deleteProduct(any(UUID.class))).thenThrow(new ProductApplicationServiceException("Error while deleting product"));

        Throwable exception = assertThrows(ProductApplicationServiceException.class, () -> {
            productApplicationService.deleteProduct(UUID.randomUUID());
        });

        assertEquals("Error while deleting product", exception.getMessage());
    }

    @Test
    void getProductGoodTest() {
        when(productService.getProduct(productId)).thenReturn(Optional.of(product));
        assertEquals(ProductMapper.mapProductToProductResponse(product), productApplicationService.getProduct(productId));
    }

    @Test
    void getProductExceptionTest() {
        when(productService.getProduct(any(UUID.class))).thenThrow(new ProductApplicationServiceException("Error while getting product"));

        Throwable exception = assertThrows(ProductApplicationServiceException.class, () -> {
            productApplicationService.getProduct(invalidProductRequest.getId());
        });

        assertEquals("Error while getting product", exception.getMessage());
    }

    @Test
    void getAllProductsGoodTest() {
        HashSet<Product> products = new HashSet<>();
        products.add(product);
        when(productService.getAllProducts(false)).thenReturn(products);

        assertEquals(productResponses, productApplicationService.getAllProducts(false));
    }

    @Test
    void getAllProductsExceptionTest() {

        when(productService.getAllProducts(false)).thenThrow(new ProductApplicationServiceException("Error while getting products"));

        Throwable exception = assertThrows(ProductApplicationServiceException.class, () -> {
            productApplicationService.getAllProducts(false);
        });

        assertEquals("Error while getting products", exception.getMessage());

    }

    @Test
    void findProductBySearchKeyGoodTest() {


        when(productService.findProductsBySearchKey("Product 1")).thenReturn(productList);

        List<ProductResponse> productResponses = new ArrayList<>();
        productResponses.add(ProductMapper.mapProductToProductResponse(product));

        assertEquals(productResponses, productApplicationService.findProductBySearchKey("Product 1"));
    }

    @Test
    void findProductByCategoryExceptionTest() {
        when(productService.findProductsBySearchKey("Product 1")).thenThrow(new ProductApplicationServiceException("Error while getting products"));

        Throwable exception = assertThrows(ProductApplicationServiceException.class, () -> {
            productApplicationService.findProductBySearchKey("Product 1");
        });

        assertEquals("Error while getting products", exception.getMessage());
    }

    @Test
    void findProductsInCategoryBySearchKeyGoodTest() {
        when(productService.findProductsBySearchKey("Product 1")).thenReturn(productList);

        Set<ProductResponse> productResponseList = new HashSet<>();
        productResponseList.add(ProductMapper.mapProductToProductResponse(product));
        assertEquals(productResponseList, productApplicationService.findProductsInCategoryBySearchKey(category.getId(), "Product 1"));
    }

    @Test
    void findProductsInCategoryBySearchKeyBadTest() {
        when(productService.findProductsBySearchKey("Product 1")).thenReturn(new HashSet<>());
        assertEquals(new HashSet<>(), productApplicationService.findProductsInCategoryBySearchKey(category.getId(), "Product 1"));
    }

    @Test
    void findProductsInCategoryBySearchKeyExceptionTest() {
        when(productService.findProductsBySearchKey("Product 1")).thenThrow(new ProductApplicationServiceException("Error while getting products"));

        Throwable exception = assertThrows(ProductApplicationServiceException.class, () -> {
            productApplicationService.findProductsInCategoryBySearchKey(null, "Product 1");
        });

        assertEquals("Error while getting products", exception.getMessage());
    }

}
