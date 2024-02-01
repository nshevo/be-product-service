package com.nml.core.application.mapper;

import com.nml.core.application.dto.category.CategoryRequest;
import com.nml.core.application.dto.category.CategoryResponse;
import com.nml.core.application.mapper.CategoryMapper;
import com.nml.core.domain.model.Category;
import com.nml.core.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
public class CategoryMapperUnitTests {

    Category category, secondaryCategory;
    CategoryRequest categoryRequest;
    Set<Category> categories = new HashSet<>();
    Product product;

    @BeforeEach
    public void setup(){
        UUID categoryId = UUID.randomUUID();

        product = Product.builder().build();
        product.setId(UUID.randomUUID());
        product.setName("Product Name");
        product.setQuantity(10);
        product.setPrice(100);
        product.setDeleted(false);
        product.setDetails("Details");
        product.setDescription("Description");
        product.addCategory(category);
        product.setImagelink("Image Link");

        category = Category.builder()
                .id(UUID.randomUUID())
                .description("Category 1 Description")
                .name("Category 1")
                .deleted(false)
                .build();

        secondaryCategory = Category.builder()
                .id(UUID.randomUUID())
                .description("Secondary Category Description")
                .name("Secondary Category")
                .deleted(false)
                .build();

        categories.add(category);
        categories.add(secondaryCategory);

        categoryRequest = CategoryRequest.builder()
                .name("Category Request")
                .id(UUID.randomUUID())
                .description("Category Description")
                .deleted(false)
                .build();
    }

    @Test
    void mapCategoryRequestToCategoryGoodTest(){
        Category mappedCategory = CategoryMapper.mapCategoryRequestToCategory(categoryRequest);

        assertEquals(categoryRequest.getId(), mappedCategory.getId());
        assertEquals(categoryRequest.getDescription(), mappedCategory.getDescription());
        assertEquals(categoryRequest.getName(), mappedCategory.getName());
        assertTrue(categoryRequest.isDeleted() == mappedCategory.isDeleted());
        assertTrue(mappedCategory.getProducts() == null);

    }

    @Test
    void mapCategoryToCategoryResponseGoodTest(){
        HashSet<Product> productHashSet = new HashSet<>();
        productHashSet.add(product);
        category.setProducts(productHashSet);

        CategoryResponse mappedCategoryResponse = CategoryMapper.mapCategoryToCategoryResponse(category);

        assertEquals(category.getId(), mappedCategoryResponse.getId());
        assertEquals(category.getDescription(), mappedCategoryResponse.getDescription());
        assertEquals(category.getName(), mappedCategoryResponse.getName());
        assertTrue(category.isDeleted() == mappedCategoryResponse.isDeleted());
        assertTrue(category.getProducts().size() == 1);
        assertTrue(category.getProducts().contains(product));
    }

    @Test
    void mapCategoriesToCategoryResponses(){
        Iterable<CategoryResponse> mappedCategoryResponses = CategoryMapper.mapCategoriesToCategoryResponses(categories);
        assertTrue(((HashSet)mappedCategoryResponses).size() != 0);
        for(CategoryResponse categoryResponse : mappedCategoryResponses){
            assertTrue(categoryResponse.getId() == category.getId() || categoryResponse.getId() == secondaryCategory.getId());
            assertTrue(categoryResponse.getName() != null);
            assertTrue(categoryResponse.getDescription() != null);
            assertTrue(categoryResponse.isDeleted() == false);
        }
    }

}
