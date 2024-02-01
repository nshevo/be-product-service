package com.nml.port.roles.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nml.core.application.service.exceptions.CategoryApplicationServiceException;
import com.nml.core.application.service.interfaces.ICategoryApplicationService;
import com.nml.core.domain.model.Category;
import com.nml.core.application.dto.category.CategoryRequest;
import com.nml.core.application.dto.category.CategoryResponse;
import com.nml.core.application.dto.product.ProductResponse;
import com.nml.port.config.SecurityConfig;
import com.nml.port.roles.user.controller.rest.UserCategoryController;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import({SecurityConfig.class})
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserCategoryControllerIntegrationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    Validator validator;

    @MockBean
    ICategoryApplicationService categoryApplicationService;

    @InjectMocks
    UserCategoryController userCategoryController;

    Category category, categoryInvalid;
    UUID categoryId;

    CategoryRequest categoryRequest, invalidCategoryRequest;
    CategoryResponse categoryResponse;

    ProductResponse productResponse;

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        categoryId = UUID.randomUUID();
        category = Category.builder().build();
        category.setId(categoryId);
        category.setName("Category Name");
        category.setDescription("Category Description");
        category.setDeleted(false);

        categoryRequest = CategoryRequest.builder().build();
        categoryRequest.setName("Category Name");
        categoryRequest.setDescription("Category Description");

        invalidCategoryRequest = CategoryRequest.builder().build();
        invalidCategoryRequest.setName("Category Name");
        invalidCategoryRequest.setDescription("Category Description");

        categoryResponse = CategoryResponse.builder().build();
        categoryResponse.setId(categoryId);
        categoryResponse.setName("Category Name");
        categoryResponse.setDescription("Category Description");
        categoryResponse.setDeleted(false);

        categoryInvalid = Category.builder().build();
        categoryInvalid.setId(UUID.randomUUID());

        List categories = new ArrayList();
        categories.add(category);

        productResponse = ProductResponse.builder().build();
        productResponse.setId(UUID.randomUUID());
        productResponse.setName("Product Name");
        productResponse.setDescription("Product Description");
        productResponse.setPrice(100.00);
        productResponse.setDeleted(false);
        productResponse.setCategories(categories);
        productResponse.setQuantity(1);
        productResponse.setDetails("Product Details");
        productResponse.setImagelink("Product Image Link");
    }

    @Test
    void getCategoryGoodTest() throws Exception {
        when(categoryApplicationService.getCategory(categoryId)).thenReturn(categoryResponse);

        ResponseEntity<CategoryResponse> responseEntity = restTemplate.getForEntity("/user/category/" + categoryId, CategoryResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void getCategoryBadTest() throws Exception {
        when(categoryApplicationService.getCategory(any(UUID.class))).thenReturn(null);

        ResponseEntity<CategoryResponse> responseEntity = restTemplate.getForEntity("/user/category/" + categoryId, CategoryResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void getCategoryExceptionTest() throws Exception {
        when(categoryApplicationService.getCategory(any(UUID.class))).thenThrow(new CategoryApplicationServiceException("Error while getting category"));

        ResponseEntity<String> response = restTemplate.getForEntity("/user/category/" + categoryId, String.class);

        assertEquals("Error while getting category", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getAllCategoriesGoodTest() throws Exception {
        List<CategoryResponse> categoryResponseList = new ArrayList<>();
        categoryResponseList.add(categoryResponse);

        when(categoryApplicationService.getAllCategories(false)).thenReturn(categoryResponseList);

        ParameterizedTypeReference<List<CategoryResponse>> responseType = new ParameterizedTypeReference<List<CategoryResponse>>() {};
        ResponseEntity<List<CategoryResponse>> responseEntity = restTemplate.exchange("/user/categories", HttpMethod.GET, null, responseType);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(categoryResponseList, responseEntity.getBody());
    }

    @Test
    void getAllCategoriesBadTest() throws Exception {

        when(categoryApplicationService.getAllCategories(false)).thenReturn(new HashSet<>());

        ParameterizedTypeReference<List<CategoryResponse>> responseType = new ParameterizedTypeReference<List<CategoryResponse>>() {};
        ResponseEntity<List<CategoryResponse>> responseEntity = restTemplate.exchange("/user/categories", HttpMethod.GET, null, responseType);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void findCategoriesBySearchKeyGoodTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Set<CategoryResponse> categoryResponseSet = new HashSet<>();
        categoryResponseSet.add(categoryResponse);

        String searchKey = "Category Name";
        when(categoryApplicationService.findCategoryBySearchKey(searchKey)).thenReturn(categoryResponseSet);

        ParameterizedTypeReference<Set<CategoryResponse>> responseType = new ParameterizedTypeReference<Set<CategoryResponse>>() {};
        ResponseEntity<Set<CategoryResponse>> responseEntity = restTemplate.exchange(
                "/user/findCategories?searchKey={searchKey}", HttpMethod.GET, null, responseType, searchKey);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(categoryResponseSet, responseEntity.getBody());
    }

    @Test
    void findCategoriesBySearchKeyBadTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String searchKey = "Category Name";
        when(categoryApplicationService.findCategoryBySearchKey(searchKey)).thenReturn(new HashSet<>());

        ParameterizedTypeReference<Set<CategoryResponse>> responseType = new ParameterizedTypeReference<Set<CategoryResponse>>() {};
        ResponseEntity<Set<CategoryResponse>> responseEntity = restTemplate.exchange(
                "/user/findCategories?searchKey={searchKey}", HttpMethod.GET, null, responseType, searchKey);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    void getProductsByCategoryGoodTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<ProductResponse> productResponseList = new ArrayList<>();
        productResponseList.add(productResponse);

        when(categoryApplicationService.getProductsByCategoryId(categoryId)).thenReturn(productResponseList);

        ParameterizedTypeReference<Iterable<ProductResponse>> responseType = new ParameterizedTypeReference<Iterable<ProductResponse>>() {};
        ResponseEntity<Iterable<ProductResponse>> responseEntity = restTemplate.exchange(
                "/user/category/" + categoryId +"/products", HttpMethod.GET, null, responseType);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(productResponseList, responseEntity.getBody());
        assertTrue(productResponseList.containsAll((ArrayList)responseEntity.getBody()));
    }

    @Test
    void getProductsByCategoryBadTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<ProductResponse> productResponseList = new ArrayList<>();

        when(categoryApplicationService.getProductsByCategoryId(categoryId)).thenReturn(null);

        ParameterizedTypeReference<Iterable<ProductResponse>> responseType = new ParameterizedTypeReference<Iterable<ProductResponse>>() {};
        ResponseEntity<Iterable<ProductResponse>> responseEntity = restTemplate.exchange(
                "/user/category/" + categoryId +"/products", HttpMethod.GET, null, responseType);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        assertEquals(null, responseEntity.getBody());
    }

    @Test
    void getProductsByCategoryExceptionTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        when(categoryApplicationService.getProductsByCategoryId(categoryId)).thenThrow(new CategoryApplicationServiceException("Error while getting products by category"));

        ParameterizedTypeReference<Iterable<ProductResponse>> responseType = new ParameterizedTypeReference<Iterable<ProductResponse>>() {};
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/user/category/" + categoryId +"/products", HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Error while getting products by category", responseEntity.getBody());

    }

}
