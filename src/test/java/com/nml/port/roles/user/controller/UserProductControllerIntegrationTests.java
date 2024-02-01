package com.nml.port.roles.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nml.core.application.dto.product.ProductRequest;
import com.nml.core.application.dto.product.ProductResponse;
import com.nml.core.application.service.exceptions.ProductApplicationServiceException;
import com.nml.core.application.service.impl.ProductApplicationService;
import com.nml.core.domain.model.Category;
import com.nml.core.domain.model.Product;
import com.nml.core.domain.service.impl.ProductService;
import com.nml.port.config.SecurityConfig;
import com.nml.port.roles.user.controller.rest.UserProductController;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import({SecurityConfig.class})
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserProductControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    Validator validator;

    @Mock
    ProductService productService;

    @MockBean
    ProductApplicationService productApplicationService;

    @InjectMocks
    UserProductController userProductController;

    Product product, productInvalid;
    UUID productId;

    ProductRequest productRequest, invalidProductRequest;
    ProductResponse productResponse;

    Category fruits, dairy, meat, vegetables;

    List<ProductResponse> productList;

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setup() {

        fruits = Category.builder().id(UUID.randomUUID()).name("Fruits").description("Fruit products").deleted(false).build();
        dairy = Category.builder().id(UUID.randomUUID()).name("Dairy").description("Dairy products").deleted(false).build();
        meat = Category.builder().id(UUID.randomUUID()).name("Meat").description("Meat products").deleted(false).build();
        vegetables = Category.builder().id(UUID.randomUUID()).name("Vegetables").description("Vegetable products").deleted(false).build();

        MockitoAnnotations.openMocks(this);
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
        productRequest.setCategories(new ArrayList<>(Set.of(vegetables)));
        productRequest.setImagelink("Image Link");

        productResponse = ProductResponse.builder().build();
        productResponse.setName("Product Name");
        productResponse.setId(productId);
        productResponse.setQuantity(10);
        productResponse.setPrice(100);
        productResponse.setDeleted(false);
        productResponse.setDetails("Details");
        productResponse.setDescription("Description");
        productResponse.setCategories(new ArrayList<>(Set.of(vegetables)));
        productResponse.setImagelink("Image Link");

        UUID unknownUUID = UUID.randomUUID();

        invalidProductRequest = ProductRequest.builder().build();
        invalidProductRequest.setId(unknownUUID);

        productInvalid = Product.builder().build();
        productInvalid.setId(UUID.randomUUID());

        productList = new ArrayList<>();
        productList.add(productResponse);
    }

    @Test
    void getProductGoodTest(){
        when(productApplicationService.getProduct(productId)).thenReturn(productResponse);

        ResponseEntity<ProductResponse> responseEntity = restTemplate.getForEntity("/user/product/" + productId, ProductResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void getProductBadTest() {
        when(productApplicationService.getProduct(any(UUID.class))).thenReturn(null);

        ResponseEntity<ProductResponse> responseEntity = restTemplate.getForEntity("/user/product/" + productId, ProductResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void getProductExceptionTest() throws Exception {
        when(productApplicationService.getProduct(any(UUID.class))).thenThrow(new ProductApplicationServiceException("Error while getting product"));

        ResponseEntity<String> response = restTemplate.getForEntity("/user/product/" + productId, String.class);

        assertEquals("Error while getting product", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getProductInternalExceptionTest() {
        ResponseEntity<String> response = restTemplate.getForEntity("/user/product/" + null, String.class);

        assertEquals( "An unexpected error occurred", response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void getProductsGoodTest(){
        when(productApplicationService.getAllProducts(false)).thenReturn(new ArrayList<>(List.of(productResponse)));

        ParameterizedTypeReference<Iterable<ProductResponse>> responseType = new ParameterizedTypeReference<Iterable<ProductResponse>>() {};

        ResponseEntity<Iterable<ProductResponse>> responseEntity = restTemplate.exchange(
                "/user/products", HttpMethod.GET, null, responseType);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void getProductsBadTest(){
        when(productApplicationService.getAllProducts(false)).thenReturn(null);

        ParameterizedTypeReference<Iterable<ProductResponse>> responseType = new ParameterizedTypeReference<Iterable<ProductResponse>>() {};

        ResponseEntity<Iterable<ProductResponse>> responseEntity = restTemplate.exchange(
                "/user/products", HttpMethod.GET, null, responseType);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void getProductsExceptionTest(){
        when(productApplicationService.getAllProducts(false)).thenThrow(new ProductApplicationServiceException("Error while getting products"));

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/user/products", HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals( "Error while getting products", responseEntity.getBody());
    }

    @Test
    void findProductsBySearchKeyGoodTest() throws Exception {
        Set<ProductResponse> productResponseSet = new HashSet<>();
        productResponseSet.add(productResponse);

        String searchKey = "Product Name";
        when(productApplicationService.findProductBySearchKey(searchKey)).thenReturn(productResponseSet);

        ParameterizedTypeReference<Set<ProductResponse>> responseType = new ParameterizedTypeReference<Set<ProductResponse>>() {};
        ResponseEntity<Set<ProductResponse>> responseEntity = restTemplate.exchange(
                "/user/findProducts?searchKey={searchKey}", HttpMethod.GET, null, responseType, searchKey);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(productResponseSet, responseEntity.getBody());
    }

    @Test
    void findProductsBySearchKeyBadTest() throws Exception {
        String searchKey = "Product Name";
        when(productApplicationService.findProductBySearchKey(searchKey)).thenReturn(new HashSet<>());

        ParameterizedTypeReference<Set<ProductResponse>> responseType = new ParameterizedTypeReference<Set<ProductResponse>>() {};
        ResponseEntity<Set<ProductResponse>> responseEntity = restTemplate.exchange(
                "/user/findProducts?searchKey={searchKey}", HttpMethod.GET, null, responseType, searchKey);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    void findProductInCategoryBySearchKeyGoodTest(){
        Set<ProductResponse> productResponseSet = new HashSet<>();
        productResponseSet.add(productResponse);

        String searchKey = "Product Name";
        when(productApplicationService.findProductsInCategoryBySearchKey(vegetables.getId(), searchKey)).thenReturn(productResponseSet);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/user/findProductsInCategory")
                .queryParam("categoryId", vegetables.getId())
                .queryParam("searchKey", searchKey);

        String url = builder.build().toString();

        ParameterizedTypeReference<HashSet<ProductResponse>> responseType = new ParameterizedTypeReference<HashSet<ProductResponse>>() {};

        ResponseEntity<HashSet<ProductResponse>> responseEntity = restTemplate.exchange(
                url, HttpMethod.GET, null, responseType);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(productResponseSet, responseEntity.getBody());
    }

    @Test
    void findProductInCategoryBySearchKeyBadTest(){
        Set<ProductResponse> productResponseSet = new HashSet<>();
        productResponseSet.add(productResponse);

        String searchKey = "Product Name";
        when(productApplicationService.findProductsInCategoryBySearchKey(vegetables.getId(), searchKey)).thenReturn(null);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/user/findProductsInCategory")
                .queryParam("categoryId", vegetables.getId())
                .queryParam("searchKey", searchKey);

        String url = builder.build().toString();

        ParameterizedTypeReference<HashSet<ProductResponse>> responseType = new ParameterizedTypeReference<HashSet<ProductResponse>>() {};

        ResponseEntity<HashSet<ProductResponse>> responseEntity = restTemplate.exchange(
                url, HttpMethod.GET, null, responseType);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void findProductInCategoryBySearchKeyExceptionTest(){
        when(productApplicationService.findProductsInCategoryBySearchKey(any(UUID.class), any(String.class))).thenThrow(new ProductApplicationServiceException("Error while getting products"));

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/user/findProductsInCategory")
                .queryParam("categoryId", UUID.randomUUID())
                .queryParam("searchKey", "searchKey");

        String url = builder.build().toString();

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url, HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}

