package com.nml.port.roles.admin.controller;

import com.nml.core.application.dto.product.ProductRequest;
import com.nml.core.application.dto.product.ProductResponse;
import com.nml.core.application.service.impl.ProductApplicationService;
import com.nml.core.domain.model.Category;
import com.nml.core.domain.model.Product;
import com.nml.port.config.SecurityConfig;
import com.nml.port.roles.admin.controller.rest.AdminProductController;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import({SecurityConfig.class})
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminProductControllerIntegrationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    Validator validator;

    @MockBean
    ProductApplicationService productApplicationService;

    @InjectMocks
    AdminProductController adminProductController;

    Product product, productInvalid;
    UUID productId;

    ProductRequest productRequest, invalidProductRequest;
    ProductResponse productResponse;

    Category fruits, dairy, meat, vegetables;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        fruits = Category.builder().id(UUID.randomUUID()).name("Fruits").description("Fruit products").deleted(false).build();
        dairy = Category.builder().id(UUID.randomUUID()).name("Dairy").description("Dairy products").deleted(false).build();
        meat = Category.builder().id(UUID.randomUUID()).name("Meat").description("Meat products").deleted(false).build();
        vegetables = Category.builder().id(UUID.randomUUID()).name("Vegetables").description("Vegetable products").deleted(false).build();

        productId = UUID.randomUUID();
        product = Product.builder().build();
        product.setId(productId);
        product.setName("Product Name");
        product.setQuantity(10);
        product.setPrice(100);
        product.setDeleted(false);
        product.setDetails("Details");
        product.setDescription("Description");
        product.addCategory(vegetables);
        product.setImagelink("Image Link");

        productRequest = productRequest.builder().build();
        productRequest.setName("Product Name");
        productRequest.setId(productId);
        productRequest.setQuantity(10);
        productRequest.setPrice(100);
        productRequest.setDeleted(false);
        productRequest.setDetails("Details");
        productRequest.setDescription("Description");
        productRequest.setCategories(new HashSet<>(Set.of(vegetables)));
        productRequest.setImagelink("Image Link");

        productResponse = ProductResponse.builder().build();
        productResponse.setName("Product Name");
        productResponse.setId(productId);
        productResponse.setQuantity(10);
        productResponse.setPrice(100);
        productResponse.setDeleted(false);
        productResponse.setDetails("Details");
        productResponse.setDescription("Description");
        productResponse.setCategories(new HashSet<>(Set.of(vegetables)));
        productResponse.setImagelink("Image Link");

        UUID unknownUUID = UUID.randomUUID();

        invalidProductRequest = ProductRequest.builder().build();
        invalidProductRequest.setId(unknownUUID);

        productInvalid = Product.builder().build();
        productInvalid.setId(UUID.randomUUID());
    }

    @Test
    void createProductGoodTest() throws Exception{
        when(productApplicationService.createProduct(any(ProductRequest.class))).thenReturn(true);

        ResponseEntity<String> response = restTemplate.exchange("/admin/product", HttpMethod.POST, new HttpEntity<>(productRequest), String.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Product successfully created", response.getBody());

        verify(productApplicationService, times(1)).createProduct(any(ProductRequest.class));

    }

    @Test
    void createProductBadTest() throws Exception{
        when(productApplicationService.createProduct(any(ProductRequest.class))).thenReturn(false);

        ResponseEntity<String> response = restTemplate.exchange("/admin/product", HttpMethod.POST, new HttpEntity<>(productRequest), String.class);

        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Product could not be created", response.getBody());

        verify(productApplicationService, times(1)).createProduct(any(ProductRequest.class));
    }

    @Test
    void createProductExceptionTest() throws Exception{
        ResponseEntity<String> response = restTemplate.exchange("/admin/product", HttpMethod.POST, new HttpEntity<>(invalidProductRequest), String.class);

        assertTrue(response.getStatusCode().is5xxServerError());
    }

    @Test
    void updateProductGoodTest() throws Exception{
        when(productApplicationService.updateProduct(any(ProductRequest.class))).thenReturn(true);

        ResponseEntity<String> response = restTemplate.exchange("/admin/product", HttpMethod.PUT, new HttpEntity<>(productRequest), String.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Product successfully updated", response.getBody());

        verify(productApplicationService, times(1)).updateProduct(any(ProductRequest.class));
    }

    @Test
    void updateProductBadTest() throws Exception{
        when(productApplicationService.updateProduct(any(ProductRequest.class))).thenReturn(false);

        ResponseEntity<String> response = restTemplate.exchange("/admin/product", HttpMethod.PUT, new HttpEntity<>(productRequest), String.class);

        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Product could not be updated", response.getBody());

        verify(productApplicationService, times(1)).updateProduct(any(ProductRequest.class));
    }

    @Test
    void updateProductExceptionTest() throws Exception{
        ResponseEntity<String> response = restTemplate.exchange("/admin/product", HttpMethod.PUT, null, String.class);

        assertTrue(response.getStatusCode().is5xxServerError());
    }

    @Test
    void deleteProductGoodTest() throws Exception{
        when(productApplicationService.deleteProduct(any(UUID.class))).thenReturn(true);

        ResponseEntity<String> response = restTemplate.exchange("/admin/product/" + productId, HttpMethod.DELETE, null, String.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Product successfully deleted", response.getBody());

        verify(productApplicationService, times(1)).deleteProduct(any(UUID.class));
    }

    @Test
    void deleteProductBadTest() throws Exception{
        when(productApplicationService.deleteProduct(any(UUID.class))).thenReturn(false);

        ResponseEntity<String> response = restTemplate.exchange("/admin/product/" + productId, HttpMethod.DELETE, null, String.class);

        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Product could not be deleted", response.getBody());

        verify(productApplicationService, times(1)).deleteProduct(any(UUID.class));
    }

    @Test
    void deleteProductExceptionTest() throws Exception{
        ResponseEntity<String> response = restTemplate.exchange("/admin/product/" + null, HttpMethod.DELETE, null, String.class);

        assertTrue(response.getStatusCode().is5xxServerError());
    }


}
