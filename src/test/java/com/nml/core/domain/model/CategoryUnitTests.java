package com.nml.core.domain.model;

import com.nml.core.domain.service.interfaces.ICategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CategoryUnitTests {

    @Autowired
    ICategoryRepository categoryRepository;

    @Test
    public void categoryGettersTest() {
        Category category = Category.builder().name("Fruits").description("Fruit products").deleted(false).build();

        assertThat(category.getName()).isEqualTo("Fruits");
        assertThat(category.getDescription()).isEqualTo("Fruit products");
        assertThat(category.isDeleted()).isFalse();
    }

    @Test
    public void categorySettersTest() {
        Category category = Category.builder().build();

        category.setName("Dairy");
        assertThat(category.getName()).isEqualTo("Dairy");

        category.setDescription("Dairy products");
        assertThat(category.getDescription()).isEqualTo("Dairy products");

        category.setDeleted(true);
        assertThat(category.isDeleted()).isTrue();
    }

    @Test
    public void categoryConstructorsTest() {
        Category category = Category.builder().name("Meat").description("Meat products").deleted(false).build();

        assertThat(category.getName()).isEqualTo("Meat");
        assertThat(category.getDescription()).isEqualTo("Meat products");
        assertThat(category.isDeleted()).isFalse();
    }

    @Test
    public void categoryEqualsTest() {
        Category category1 = Category.builder().name("Fruits").description("Fruit products").deleted(false).build();
        Category category2 = Category.builder().name("Fruits").description("Fruit products").deleted(false).build();

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    public void categoryHashCodeTest() {
        Category category1 = Category.builder().name("Fruits").description("Fruit products").deleted(false).build();
        Category category2 = Category.builder().name("Fruits").description("Fruit products").deleted(false).build();

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        assertThat(category1.hashCode()).isNotEqualTo(category2.hashCode());
    }

    @Test
    public void categoryToStringTest() {
        Category category = Category.builder().name("Fruits").description("Fruit products").deleted(false).build();

        assertThat(category.toString()).isNotNull();
    }

    @Test
    public void categoryBuilderTest() {
        Category category = Category.builder().name("Vegetables").description("Vegetable products").deleted(false).build();

        assertThat(category.getName()).isEqualTo("Vegetables");
        assertThat(category.getDescription()).isEqualTo("Vegetable products");
        assertThat(category.isDeleted()).isFalse();

        categoryRepository.save(category);

        assertThat(categoryRepository.findByNameIgnoreCaseContaining("Vegetables")).isNotNull();
    }
}
