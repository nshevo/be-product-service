package com.nml.core.domain.model;

import com.nml.core.domain.service.interfaces.ICategoryRepository;
import com.nml.core.domain.service.interfaces.IProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductUnitTests {

    @Autowired
    IProductRepository productRepository;

    @Autowired
    ICategoryRepository categoryRepository;

    Category fruits = Category.builder().id(UUID.randomUUID()).name("Fruits").description("Fruit products").deleted(false).build();
    Category meat = Category.builder().id(UUID.randomUUID()).name("Meat").description("Meat products").deleted(false).build();
    Category bio = Category.builder().id(UUID.randomUUID()).name("Bio").description("Bio products").deleted(false).build();

    @Test
    public void productGettersTest(){
        Product product = Product.builder().name("Chicken breasts 1 kg").price(100.00).categories(new HashSet<>(Set.of(meat)))
                .description("Tasty, BIO, good").details("Made in Brandenburg").imagelink("").quantity(100).build();

        assertThat(product.getCategories().contains(meat));
        assertThat(product.getName().equals("Chicken breasts 1 kg"));
        assertThat(product.getPrice() == 100.00);
        assertThat(product.getDescription().equals("Tasty, BIO, good"));
        assertThat(product.getDetails().equals("Made in Brandenburg"));
        assertThat(product.getQuantity() == 100);
        assertThat(product.isDeleted() == false);
        assertThat(product.getImagelink().equals(""));
    }

    @Test
    public void productSettersTest(){
        Product product = Product.builder().build();

        product.addCategory(fruits);
        assertThat(product.getCategories().contains(fruits));

        product.setDescription("Fresh bananas from Equador");
        assertThat(product.getDescription().equals("Fresh bananas from Equador"));

        product.setDetails("Almost perfectly ripe and very sweet");
        assertThat(product.getDetails().equals("Almost perfectly ripe and very sweet"));

        product.setPrice(2.99);
        assertThat(product.getPrice() == 2.99);

        product.setName("Bananas 1 kg");
        assertThat(product.getName().equals("Bananas 1 kg"));

        product.setImagelink("link");
        assertThat(product.getImagelink().equals("link"));

        product.setQuantity(1000);
        assertThat(product.getQuantity() == 1000);

        UUID productId = UUID.randomUUID();
        product.setId(productId);
        assertThat(product.getId().equals(productId));

        assertThat(product.getId().toString().length() != 0);
    }

    @Test
    public void productConstructorsTest(){
        Product product = Product.builder().name("Chicken breasts 1 kg").price(100.00).categories(new HashSet<>(Set.of(meat)))
                .description("Tasty, BIO, good").details("Made in Brandenburg").imagelink("").quantity(100).build();

        assertThat(product.getCategories().contains(meat));
        assertThat(product.getName().equals("Chicken breasts 1 kg"));
        assertThat(product.getPrice() == 100.00);
        assertThat(product.getDescription().equals("Tasty, BIO, good"));
        assertThat(product.getDetails().equals("Made in Brandenburg"));
        assertThat(product.getQuantity() == 100);
        assertThat(product.isDeleted() == false);
        assertThat(product.getImagelink().equals(""));
    }

    @Test
    public void productEqualsTest(){
        categoryRepository.save(meat);
        categoryRepository.save(bio);

        Product product1 = Product.builder().name("Chicken breasts 1 kg").price(100.00).categories(new HashSet<>(Set.of(meat)))
                .description("Tasty, BIO, good").details("Made in Brandenburg").imagelink("").quantity(100).build();

        Product product2 = Product.builder().name("Chicken breasts 1 kg").price(100.00).categories(new HashSet<>(Set.of(meat, bio)))
                .description("Tasty, BIO, good").details("Made in Brandenburg").imagelink("").quantity(100).build();

        productRepository.save(product1);
        productRepository.save(product2);

        assertThat(product1.equals(product2) == false);
    }

    @Test
    public void productHashCodeTest(){
        categoryRepository.save(meat);
        categoryRepository.save(bio);

        Product product1 = Product.builder().name("Chicken breasts 1 kg").price(100.00).categories(new HashSet<>(Set.of(meat, bio)))
                .description("Tasty, BIO, good").details("Made in Brandenburg").imagelink("").quantity(100).build();

        Product product2 = Product.builder().name("Chicken breasts 1 kg").price(100.00).categories(new HashSet<>(Set.of(meat)))
                .description("Tasty, BIO, good").details("Made in Brandenburg").imagelink("").quantity(100).build();

        productRepository.save(product1);
        productRepository.save(product2);

        assertThat(product1.hashCode() != product2.hashCode());
    }

    @Test
    public void productToStringTest(){
        Product product = Product.builder().name("Chicken breasts 1 kg").price(100.00).categories(new HashSet<>(Set.of(meat)))
                .description("Tasty, BIO, good").details("Made in Brandenburg").imagelink("").quantity(100).build();

        assertThat(product.toString().length() != 0);
    }

    @Test
    public void productBuilderTest() {
        HashSet<Category> categories = new HashSet<>();
        categories.add( categoryRepository.save(meat));

        Product product = Product.builder().name("Chicken breasts 1 kg").price(100.00).categories(categories)
                .description("Tasty, BIO, good").details("Made in Brandenburg").imagelink("").quantity(100).build();

        assertThat(product.getCategories().contains(meat));
        assertThat(product.getName().equals("Chicken breasts 1 kg"));
        assertThat(product.getPrice()==100.00);
        assertThat(product.getDescription().equals("Tasty, BIO, good"));
        assertThat(product.getDetails().equals("Made in Brandenburg"));
        assertThat(product.getQuantity()==100);
        assertThat(product.isDeleted()==false);
        assertThat(product.getImagelink().equals(""));
        assertThat(product.getCategories().equals(new HashSet<>(Set.of(meat)))==true);

        productRepository.save(product);

        assertThat(product.getId().toString().length()!=0);

    }

    @Test
    void productAddCategoryTest() {
        Product product = Product.builder().name("Chicken breasts 1 kg").price(100.00).categories(new HashSet<>(Set.of(meat)))
                .description("Tasty, BIO, good").details("Made in Brandenburg").imagelink("").quantity(100).build();

        product.addCategory(bio);
        assertThat(product.getCategories().contains(bio));

        product.addCategory(fruits);
        assertThat(product.getCategories().contains(fruits));
    }




}
