package com.nml.core.domain.service.impl;

import com.nml.core.domain.exception.product.ProductsNotFoundException;
import com.nml.core.domain.exception.product.repository.ProductRepositoryException;
import com.nml.core.domain.model.Category;
import com.nml.core.domain.model.Product;
import com.nml.core.domain.service.interfaces.IProductRepository;
import com.nml.core.domain.exception.product.ProductAlreadyDeletedException;
import com.nml.core.domain.exception.product.ProductNotFoundException;
import jakarta.persistence.EntityManager;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
public class ProductServiceUnitTests {

    @Mock
    private IProductRepository productRepository;

    @Mock
    EntityManager entityManager;

    @Mock
    Session session;

    @Mock
    Filter filter;

    @Captor
    private ArgumentCaptor<String> filterNameCaptor;

    @Captor
    private ArgumentCaptor<String> parameterNameCaptor;

    @Captor
    private ArgumentCaptor<Boolean> parameterValueCaptor;

    @Captor
    private ArgumentCaptor<String> filterDisableCaptor;

    @InjectMocks
    private ProductService productService;

    Product product, invalidProduct;
    UUID productId;

    Category fruits, dairy, meat, vegetables;

    @BeforeEach
    public void setup() {
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

        UUID unknownUUID = UUID.randomUUID();

        invalidProduct = Product.builder().build();
        invalidProduct.setId(unknownUUID);
    }

    @Test
    void createProductGoodTest() {
        when(productRepository.save(product)).thenReturn(product);

        boolean productCreated = productService.createProduct(product);

        assertTrue(productCreated);
    }

    @Test
    void createProductExceptionTest() {
        when(productRepository.save(invalidProduct)).thenThrow(new ProductRepositoryException("Saving product failed"));

        Throwable exception = assertThrows(ProductRepositoryException.class, () -> {
            productService.createProduct(invalidProduct);
        });

        assertEquals("Saving product failed", exception.getMessage());
    }

    @Test
    void updateProductGoodTest(){
        when(productRepository.save(product)).thenReturn(product);

        productService.createProduct(product);
        String newName = "Test Name";

        product.setName(newName);

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        boolean productUpdated = productService.updateProduct(product);
        assertTrue(productUpdated);

        Optional<Product> updatedProduct = productService.getProduct(product.getId());

        if(updatedProduct.isPresent()){
            assertEquals(updatedProduct.get(), product);
        }else{
            fail();
        }
    }

    @Test
    void updateProductExceptionTest(){
        Throwable exception = assertThrows(ProductNotFoundException.class, () -> {
            boolean productUpdated = productService.updateProduct(product);
            assertFalse(productUpdated);
        });

        assertEquals(false, exception.getMessage().isEmpty());
    }

    @Test
    void deleteProductGoodTest() {
        when(productRepository.save(product)).thenReturn(product);
        productService.createProduct(product);

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        boolean deleted = productService.deleteProduct(product.getId());

        assertEquals(true, deleted);

        verify(productRepository, times(1)).delete(product);

        product.setDeleted(true);

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        Optional<Product> optionalProduct = productService.getProduct(product.getId());
        if(optionalProduct.isPresent()){
            assertTrue(optionalProduct.get().isDeleted());
        }

    }

    @Test
    void deleteProductRepositoryExceptionTest(){
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        doThrow(new ProductRepositoryException("Deleting product failed")).when(productRepository).delete(product);

        Throwable exception = assertThrows(ProductRepositoryException.class, () -> {
            boolean deleted = productService.deleteProduct(product.getId());
            assertEquals(false, deleted);
        });

        assertEquals("Deleting product failed", exception.getMessage());
    }

    @Test
    void deleteProductNotFoundExceptionTest(){
        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

        Throwable exception = assertThrows(ProductNotFoundException.class, () -> {

            boolean deleted = productService.deleteProduct(product.getId());
            assertEquals(false, deleted);

        });

        assertEquals(false, exception.getMessage().isEmpty());

        verify(productRepository, times(0)).delete(product);
    }

    @Test
    void deleteProductAlreadyDeletedExceptionTest() {
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        Optional<Product> optionalProduct = productService.getProduct(product.getId());

        boolean deleted = productService.deleteProduct(product.getId());
        assertEquals(true, deleted);

        product.setDeleted(deleted);

        Throwable exception = assertThrows(ProductAlreadyDeletedException.class, () -> {
           productService.deleteProduct(product.getId());
        });

        assertEquals(false, exception.getMessage().isEmpty());
    }

    @Test
    void getProductGoodTest() {
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        Optional<Product> optionalProduct = productService.getProduct(product.getId());
        assertEquals(Optional.of(product), optionalProduct);
    }

    @Test
    void getProductExceptionTest() {
        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

        Throwable exception = assertThrows(ProductNotFoundException.class, () -> {
            Optional<Product> optionalProduct = productService.getProduct(product.getId());
        });

        assertEquals(false, exception.getMessage().isEmpty());
    }

    @Test
    void findProductsBySearchKeyGoodTest(){
        String searchKey = "Product";

        when(productRepository.findBySearchCriteria(searchKey)).thenReturn(Arrays.asList(product));

        List<Product> products = (List<Product>) productService.findProductsBySearchKey("Product");

        assertEquals(1, products.size());
        assertEquals(product, products.get(0));
    }

    @Test
    void findProductsBySearchKeyExceptionTest(){
        String searchKey = "Product";

        when(productRepository.findBySearchCriteria(searchKey)).thenThrow(new ProductsNotFoundException("Could not find any products with searchKey: "+ searchKey));

        Throwable exception = assertThrows(ProductsNotFoundException.class, () -> {
            List<Product> products = (List<Product>) productService.findProductsBySearchKey(searchKey);
        });

        assertEquals(false, exception.getMessage().isEmpty());
    }

    @Test
    void getAllProductsGoodTest() {
        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(session.enableFilter("isDeletedFilter")).thenReturn(filter);
        when(filter.setParameter("deleted", false)).thenReturn(filter);

        when(productRepository.findAll()).thenReturn(createProductList());

        Iterable<Product> products = productService.getAllProducts(false);

        verify(entityManager).unwrap(Session.class);
        verify(session).enableFilter(filterNameCaptor.capture());
        verify(filter).setParameter(parameterNameCaptor.capture(), parameterValueCaptor.capture());
        verify(filter).setParameter("deleted", false);
        verify(productRepository).findAll();
        verify(session).disableFilter(filterDisableCaptor.capture());

        assertEquals("isDeletedFilter", filterNameCaptor.getValue());
        assertEquals("deleted", parameterNameCaptor.getValue());
        assertEquals(false, parameterValueCaptor.getValue());
        assertEquals("isDeletedFilter", filterDisableCaptor.getValue());

        assertTrue(((ArrayList<Product>)products).size() == 2);

    }

    @Test
    void getAllProductsExceptionTest() {
        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(session.enableFilter("isDeletedFilter")).thenReturn(filter);
        when(filter.setParameter("deleted", false)).thenReturn(filter);

        when(productRepository.findAll()).thenReturn(new ArrayList<>());

        Throwable exception = assertThrows(ProductsNotFoundException.class, () -> {
            Iterable<Product> products = productService.getAllProducts(false);
        });

        verify(entityManager).unwrap(Session.class);
        verify(session).enableFilter(filterNameCaptor.capture());
        verify(filter).setParameter(parameterNameCaptor.capture(), parameterValueCaptor.capture());
        verify(filter).setParameter("deleted", false);
        verify(productRepository).findAll();
        verify(session).disableFilter(filterDisableCaptor.capture());

        assertEquals("isDeletedFilter", filterNameCaptor.getValue());
        assertEquals("deleted", parameterNameCaptor.getValue());
        assertEquals(false, parameterValueCaptor.getValue());
        assertEquals("isDeletedFilter", filterDisableCaptor.getValue());

        assertEquals(false, exception.getMessage().isEmpty());
    }

    private List<Product> createProductList() {
        List<Product> products = new ArrayList<>();
        Product product1 = Product.builder().build();
        product1.setId(UUID.randomUUID());
        product1.setName("Product 1");
        product1.setPrice(0);
        product1.setQuantity(0);
        product1.setDescription("Description");
        product1.setImagelink("Link");
        product1.addCategory(meat);
        product1.setDeleted(false);

        Product product2 = Product.builder().build();
        product2.setId(UUID.randomUUID());
        product2.setName("Product 2");
        product2.setPrice(0);
        product2.setQuantity(0);
        product2.setDescription("Description");
        product2.setImagelink("Link");
        product2.addCategory(vegetables);
        product2.setDeleted(true);

        products.add(product1);
        products.add(product2);
        return products;
    }

}
