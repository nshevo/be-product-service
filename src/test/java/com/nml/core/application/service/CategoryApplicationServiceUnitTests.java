package com.nml.core.application.service;

import com.nml.core.application.dto.category.CategoryRequest;
import com.nml.core.application.dto.category.CategoryResponse;
import com.nml.core.application.mapper.CategoryMapper;
import com.nml.core.application.service.exceptions.CategoryApplicationServiceException;
import com.nml.core.application.service.impl.CategoryApplicationService;
import com.nml.core.application.service.interfaces.ICategoryApplicationService;
import com.nml.core.domain.model.Category;
import com.nml.core.domain.service.interfaces.ICategoryRepository;
import com.nml.core.domain.service.interfaces.ICategoryService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import net.bytebuddy.implementation.bytecode.Throw;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class CategoryApplicationServiceUnitTests {

    @Mock
    ICategoryService categoryService;

    @InjectMocks
    CategoryApplicationService categoryApplicationService;

    CategoryRequest categoryRequest, categoryRequest2, invalidCategoryRequest;
    UUID categoryId = UUID.randomUUID();
    Category category;


    @BeforeEach
    public void setup() {
        categoryRequest = CategoryRequest.builder()
                .id(categoryId)
                .description("Category Request Description")
                .deleted(false)
                .name("Category Request Name")
                .build();

        category = CategoryMapper.mapCategoryRequestToCategory(categoryRequest);

        categoryRequest2 = CategoryRequest.builder()
                .id(UUID.randomUUID())
                .description("Category Request Description")
                .deleted(false)
                .name("Category Request Name")
                .build();

        invalidCategoryRequest = CategoryRequest.builder()
                .name("Invalid Category Request Name")
                .build();
    }

    @Test
    void createCategoryGoodTest(){
        when(categoryService.createCategory(any(Category.class))).thenReturn(true);
        boolean categoryCreated = categoryApplicationService.createCategory(categoryRequest);
        assertTrue(categoryCreated);
    }

    @Test
    void updateCategoryGoodTest(){
        when(categoryService.updateCategory(any(Category.class))).thenReturn(true);
        categoryRequest.setName("Updated Category Name");
        category.setName("Updated Category Name");
        when(categoryService.getCategory(categoryRequest.getId())).thenReturn(Optional.ofNullable(category));

        boolean categoryUpdated = categoryApplicationService.updateCategory(categoryRequest);
        assertTrue(categoryUpdated);
        assertEquals("Updated Category Name", categoryApplicationService.getCategory(categoryId).getName());
    }

    @Test
    void updateCategoryExceptionTest(){
        when(categoryService.updateCategory(any(Category.class))).thenThrow(new CategoryApplicationServiceException("Error while updating category"));

        Throwable exception = assertThrows(CategoryApplicationServiceException.class, () -> {
            categoryApplicationService.updateCategory(invalidCategoryRequest);
        });
        assertEquals("Error while updating category", exception.getMessage());

    }

    @Test
    void deleteCategoryGoodTest(){
        when(categoryService.deleteCategory(categoryRequest.getId())).thenReturn(true);
        boolean categoryDeleted = categoryApplicationService.deleteCategory(categoryRequest.getId());
        assertTrue(categoryDeleted);
    }

    @Test
    void deleteCategoryExceptionTest() {
        when(categoryService.deleteCategory(any(UUID.class))).thenThrow(new CategoryApplicationServiceException("Error while deleting category"));
        Throwable exception = assertThrows(CategoryApplicationServiceException.class, () -> {
            categoryApplicationService.deleteCategory(UUID.randomUUID());
        });
        assertEquals("Error while deleting category", exception.getMessage());
    }

    @Test
    void getCategoryGoodTest(){
        when(categoryService.getCategory(categoryRequest.getId())).thenReturn(Optional.ofNullable(CategoryMapper.mapCategoryRequestToCategory(categoryRequest)));

        CategoryResponse category = categoryApplicationService.getCategory(categoryId);
        assertEquals(categoryId, category.getId());
    }

    @Test
    void getCategoryExceptionTest(){
        when(categoryService.getCategory(any(UUID.class))).thenThrow(new RuntimeException("Error while getting category"));

        Throwable exception = assertThrows(RuntimeException.class, () -> {
            categoryApplicationService.getCategory(UUID.randomUUID());
        });
        assertEquals("Error while getting category", exception.getMessage());
    }

    @Test
    void getProductsByCategoryIdGoodTest(){
        when(categoryService.getCategory(categoryRequest.getId())).thenReturn(Optional.ofNullable(CategoryMapper.mapCategoryRequestToCategory(categoryRequest)));

        CategoryResponse categoryResponse = categoryApplicationService.getCategory(categoryId);
        assertEquals(categoryId, categoryResponse.getId());
    }

    @Test
    void getProductsByCategoryIdBadTest(){
        when(categoryService.getCategory(any(UUID.class))).thenReturn(Optional.empty());

        CategoryResponse category = categoryApplicationService.getCategory(UUID.randomUUID());
        assertNull(category);
    }

    @Test
    void getProductsByCategoryIdExceptionTest(){
        when(categoryService.getProductsByCategoryId(any(UUID.class))).thenThrow(new CategoryApplicationServiceException("Error while getting products by category"));

        Throwable exception = assertThrows(CategoryApplicationServiceException.class, () -> {
            categoryApplicationService.getProductsByCategoryId(UUID.randomUUID());
        });

        assertEquals("Error while getting products by category", exception.getMessage());
    }

    @Test
    void getAllCategoriesGoodTest(){
        when(categoryService.getAllCategories(false)).thenReturn((getHashSetWith(category)));

        HashSet<CategoryResponse> categories = (HashSet<CategoryResponse>) categoryApplicationService.getAllCategories(false);
        assertEquals(1, categories.size());
    }

    @Test
    void getAllCategoriesBadTest(){
        when(categoryService.createCategory(any(Category.class))).thenReturn(true);

        boolean categoryCreated = categoryApplicationService.createCategory(categoryRequest);
        assertTrue(categoryCreated);

        when(categoryService.getAllCategories(false)).thenReturn(getHashSetWith(category));
        HashSet<CategoryResponse> categories = (HashSet<CategoryResponse>) categoryApplicationService.getAllCategories(false);
        assertEquals(1, categories.size());
    }

    private Iterable<Category> getHashSetWith(Category category) {

        HashSet<Category> categoryHashSet = new HashSet<>();
        categoryHashSet.add(category);

        return categoryHashSet;
    }

    @Test
    void getAllCategoriesExceptionTest(){

        when(categoryService.getAllCategories(false)).thenThrow(new CategoryApplicationServiceException("Error while getting all categories"));

        Throwable exception = assertThrows(CategoryApplicationServiceException.class, () -> {
            categoryApplicationService.getAllCategories(false);
        });

        assertEquals("Error while getting all categories", exception.getMessage());
    }

    @Test
    void findCategoryBySearchKeyGoodTest(){
        when(categoryService.findCategoriesBySearchKey(any(String.class))).thenReturn(getHashSetWith(category));
        HashSet<CategoryResponse> categories = (HashSet<CategoryResponse>) categoryApplicationService.findCategoryBySearchKey("Category Request Name");
        assertEquals(1, categories.size());
    }

    @Test
    void findCategoryBySearchKeyBadTest(){
        when(categoryService.findCategoriesBySearchKey(any(String.class))).thenReturn(new ArrayList<>());
        HashSet<CategoryResponse> categories = (HashSet<CategoryResponse>) categoryApplicationService.findCategoryBySearchKey("Invalid Category Request Name");
        assertEquals(0, categories.size());
    }

    @Test
    void findCategoryBySearchKeyExceptionTest(){
        when(categoryService.findCategoriesBySearchKey(any(String.class))).thenThrow(new CategoryApplicationServiceException("Error while finding category by search key"));

        Throwable exception = assertThrows(CategoryApplicationServiceException.class, () -> {
            categoryApplicationService.findCategoryBySearchKey("Invalid Category Request Name");
        });
        assertEquals("Error while finding categories by search key", exception.getMessage());

    }

}
