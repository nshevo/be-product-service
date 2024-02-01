package com.nml.core.domain.service.impl;

import com.nml.core.domain.exception.category.CategoriesNotFoundException;
import com.nml.core.domain.exception.category.repository.CategoryRepositoryException;
import com.nml.core.domain.model.Category;
import com.nml.core.domain.model.Product;
import com.nml.core.domain.service.interfaces.ICategoryRepository;
import com.nml.core.domain.exception.category.CategoryAlreadyDeletedException;
import com.nml.core.domain.exception.category.CategoryNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
public class CategoryServiceUnitTests {

    @Mock
    EntityManager entityManager;

    @Mock
    Session session;

    @Mock
    Filter filter;

    @Mock
    private ICategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    Category category, invalidCategory;
    UUID categoryId;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        categoryId = UUID.randomUUID();
        category = Category.builder().build();
        category.setId(categoryId);
        category.setName("Category Name");
        category.setDescription("Category Description");
        category.setDeleted(false);
        category.setProducts(new HashSet<>(Arrays.asList(Product.builder().build())));

        UUID unknownUUID = UUID.randomUUID();

        invalidCategory = Category.builder().build();
        invalidCategory.setId(unknownUUID);
    }

    @Test
    void createCategoryGoodTest() {
        when(categoryRepository.save(category)).thenReturn(category);

        boolean categoryCreated = categoryService.createCategory(category);

        verify(categoryRepository, times(1)).save(category);

        assertTrue(categoryCreated);
    }

    @Test
    void createCategoryBadTest() {
        when(categoryRepository.save(category)).thenThrow(new CategoryRepositoryException("Saving category failed"));

        Throwable exception = assertThrows(CategoryRepositoryException.class, () -> {
           categoryService.createCategory(category);
        });

        verify(categoryRepository, times(1)).save(category);

        assertEquals("Saving category failed", exception.getMessage());
    }

    @Test
    void updateCategoryGoodTest() {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        boolean categoryUpdated = categoryService.updateCategory(category);
        assertTrue(categoryUpdated);
    }

    @Test
    void updateCategoryBadTest() {
        UUID invalidId = invalidCategory.getId();

        when(categoryRepository.findById(invalidId)).thenThrow(new CategoryNotFoundException(invalidId));

        Throwable exception = assertThrows(CategoryNotFoundException.class, () -> {
            boolean categoryUpdated = categoryService.updateCategory(invalidCategory);
            assertFalse(categoryUpdated);
        });

        assertEquals(false, exception.getMessage().isEmpty());
    }

    @Test
    void deleteCategoryGoodTest() {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        boolean deleted = categoryService.deleteCategory(category.getId());

        assertEquals(true, deleted);

        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    void deleteCategoryNotFoundExceptionTest() {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.empty());

        Throwable exception = assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.deleteCategory(category.getId());
        });

        assertEquals(false, exception.getMessage().isEmpty());

        verify(categoryRepository, times(0)).delete(category);
    }

    @Test
    void deleteCategoryAlreadyDeletedExceptionTest() {
        category.setDeleted(true);

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        Throwable exception = assertThrows(CategoryAlreadyDeletedException.class, () -> {
            categoryService.deleteCategory(category.getId());
        });

        assertEquals(false, exception.getMessage().isEmpty());
    }

    @Test
    void deleteCategoryRepositoryExceptionTest(){
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        doThrow(new RuntimeException()).when(categoryRepository).delete(category);

        Throwable exception = assertThrows(CategoryRepositoryException.class, () -> {
            categoryService.deleteCategory(category.getId());
        });

        assertEquals(false, exception.getMessage().isEmpty());
        assertEquals("Deleting category failed", exception.getMessage());
    }

    @Test
    void getCategoryGoodTest() {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        Optional<Category> optionalCategory = categoryService.getCategory(category.getId());

        if(optionalCategory.isPresent()){
            assertEquals(optionalCategory.get(), category);
        }else {
            fail();
        }
    }

    @Test
    void getCategoryExceptionTest() {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.empty());

        Throwable exception = assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.getCategory(category.getId());
        });

        assertEquals(false, exception.getMessage().isEmpty());
    }

    @Test
    void findCategoriesBySearchKeyGoodTest() {
        String searchKey = "searchKey";
        List<Category> categories = new ArrayList<>();
        categories.add(category);

        when(categoryRepository.findBySearchCriteria(searchKey)).thenReturn(categories);

        List<Category> foundCategories = (List<Category>) categoryService.findCategoriesBySearchKey(searchKey);

        assertEquals(categories, foundCategories);
    }

    @Test
    void findCategoriesBySearchKeyExceptionTest() {
        String searchKey = "searchKey";

        when(categoryRepository.findBySearchCriteria(searchKey)).thenReturn(new ArrayList<>());

        Throwable exception = assertThrows(CategoriesNotFoundException.class, () -> {
            categoryService.findCategoriesBySearchKey(searchKey);
        });

        assertEquals(false, exception.getMessage().isEmpty());
    }

    @Test
    void getAllCategoriesGoodTest() {
        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(session.enableFilter("isDeletedFilter")).thenReturn(filter);
        when(filter.setParameter("deleted", false)).thenReturn(filter);

        List<Category> categories = new ArrayList<>();
        categories.add(category);

        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> foundCategories = (List<Category>) categoryService.getAllCategories(false);

        assertEquals(categories, foundCategories);
    }

    @Test
    void getAllCategoriesExceptionTest() {
        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(session.enableFilter("isDeletedFilter")).thenReturn(filter);
        when(filter.setParameter("deleted", false)).thenReturn(filter);

        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());

        Throwable exception = assertThrows(CategoriesNotFoundException.class, () -> {
            categoryService.getAllCategories(false);
        });

        assertEquals(false, exception.getMessage().isEmpty());
    }

    @Test
    void getProductsByCategoryIdGoodTest() {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        List<Product> products = new ArrayList<>();
        products.add(Product.builder().build());

        List<Product> foundProducts = (List<Product>) categoryService.getProductsByCategoryId(category.getId());

        assertEquals(products, foundProducts);
    }

    @Test
    void getProductsByCategoryIdExceptionTest() {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.empty());

        Throwable exception = assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.getProductsByCategoryId(category.getId());
        });

        assertFalse(exception.getMessage().isEmpty());
    }


}
