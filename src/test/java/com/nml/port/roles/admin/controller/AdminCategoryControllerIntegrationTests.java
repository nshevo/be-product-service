package com.nml.port.roles.admin.controller;

import com.nml.core.application.service.exceptions.CategoryApplicationServiceException;
import com.nml.core.application.service.interfaces.ICategoryApplicationService;
import com.nml.core.domain.model.Category;
import com.nml.core.application.dto.category.CategoryRequest;
import com.nml.core.application.dto.category.CategoryResponse;
import com.nml.port.config.SecurityConfig;
import com.nml.port.roles.admin.controller.rest.AdminCategoryController;
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import({SecurityConfig.class})
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminCategoryControllerIntegrationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    Validator validator;

    @MockBean
    ICategoryApplicationService categoryApplicationService;

    @InjectMocks
    AdminCategoryController adminCategoryController;

    CategoryRequest categoryRequest, invalidCategoryRequest;
    CategoryResponse categoryResponse;

    Category category, categoryInvalid;
    UUID categoryId;

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
        categoryRequest.setId(categoryId);

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
    }

    @Test
    void createCategoryGoodTest() throws Exception {
        when(categoryApplicationService.createCategory(categoryRequest)).thenReturn(true);

        ResponseEntity<String> response = restTemplate.exchange("/admin/category", HttpMethod.POST, new HttpEntity<>(categoryRequest), String.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Category successfully created", response.getBody());

        verify(categoryApplicationService, times(1)).createCategory(categoryRequest);
    }

    @Test
    void createCategoryBadTest() throws Exception {
        // in@Valid returns 400 Bad Request
        ResponseEntity<String> response = restTemplate.exchange("/admin/category", HttpMethod.POST, new HttpEntity<>(invalidCategoryRequest), String.class);

        assertTrue(response.getStatusCode().is4xxClientError());

        verify(categoryApplicationService, times(1)).createCategory(invalidCategoryRequest);
    }

    @Test
    void createCategoryExceptionTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response = restTemplate.exchange("/admin/category", HttpMethod.POST, new HttpEntity<>("{}", headers), String.class);

        assertTrue(response.getStatusCode().is5xxServerError());
    }

    @Test
    void updateCategoryGoodTest() throws Exception {
        when(categoryApplicationService.updateCategory(categoryRequest)).thenReturn(true);

        ResponseEntity<String> response = restTemplate.exchange("/admin/category", HttpMethod.PUT, new HttpEntity<>(categoryRequest), String.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Category successfully updated", response.getBody());

        verify(categoryApplicationService, times(1)).updateCategory(categoryRequest);
    }

    @Test
    void updateCategoryBadTest() throws Exception {
        categoryRequest.setId(null);

        ResponseEntity<String> response = restTemplate.exchange("/admin/category", HttpMethod.PUT, new HttpEntity<>(categoryRequest), String.class);
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Category could not be updated", response.getBody());

        verify(categoryApplicationService, times(1)).updateCategory(categoryRequest);
    }

    @Test
    void updateCategoryNotFoundTest() throws Exception {
        when(categoryApplicationService.updateCategory(categoryRequest)).thenThrow(new CategoryApplicationServiceException("Error while updating category"));

        ResponseEntity<String> response = restTemplate.exchange("/admin/category", HttpMethod.PUT, new HttpEntity<>(categoryRequest), String.class);

        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Error while updating category", response.getBody());

        verify(categoryApplicationService, times(1)).updateCategory(categoryRequest);
    }

    @Test
    public void deleteCategoryGoodTest() throws Exception {
        when(categoryApplicationService.deleteCategory(categoryId)).thenReturn(true);

        ResponseEntity resultString = restTemplate.exchange("/admin/category/"+categoryId, HttpMethod.DELETE, null, String.class);

        assertEquals(HttpStatus.OK, resultString.getStatusCode());
        assertEquals("Category successfully deleted", resultString.getBody());

        verify(categoryApplicationService, times(1)).deleteCategory(categoryId);
    }

    @Test
    void deleteCategoryExceptionTest() throws Exception {
        when(categoryApplicationService.deleteCategory(categoryId)).thenThrow(new CategoryApplicationServiceException("Error while deleting category"));

        ResponseEntity<String> resultString = restTemplate.exchange("/admin/category/"+categoryId, HttpMethod.DELETE, null, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, resultString.getStatusCode());
        assertEquals("Error while deleting category", resultString.getBody());

        verify(categoryApplicationService, times(1)).deleteCategory(categoryId);
    }


}
