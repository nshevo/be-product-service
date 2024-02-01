package com.nml.core.domain.service.impl;

import com.nml.core.domain.exception.product.ProductsNotFoundException;
import com.nml.core.domain.exception.product.repository.ProductRepositoryException;
import com.nml.core.domain.model.Product;
import com.nml.core.domain.service.interfaces.IProductRepository;
import com.nml.core.domain.service.interfaces.IProductService;
import com.nml.core.domain.exception.product.ProductAlreadyDeletedException;
import com.nml.core.domain.exception.product.ProductNotFoundException;
import jakarta.persistence.EntityManager;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    public ProductService(IProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public boolean saveProduct(Product product){
        try{
            productRepository.save(product);
            return true;
        }catch (RuntimeException e) {
            throw new ProductRepositoryException("Saving product failed",e);
        }
    }

    @Override
    public boolean createProduct (Product product) {
        return saveProduct(product);
    }

    @Override
    public boolean updateProduct(Product product) {

        Optional<Product> existingProductOptional = productRepository.findById(product.getId());

        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();

            existingProduct.setName(product.getName());
            existingProduct.setQuantity(product.getQuantity());
            existingProduct.setDetails(product.getDetails());
            existingProduct.setCategories(product.getCategories());
            existingProduct.setImagelink(product.getImagelink());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setDeleted(product.isDeleted());

            return saveProduct(existingProduct);
        } else {
            throw new ProductNotFoundException(product.getId());
        }
    }

    @Override
    public boolean deleteProduct(UUID productId) {
       Optional<Product> productToBeDeleted = productRepository.findById(productId);

        if(productToBeDeleted.isPresent()){
            if(productToBeDeleted.get().isDeleted()){
                throw new ProductAlreadyDeletedException(productId);
            }
            try {
                productRepository.delete(productToBeDeleted.get());
                return true;
            }catch (RuntimeException e) {
                throw new ProductRepositoryException("Deleting product failed",e);
            }
        }else{
            throw new ProductNotFoundException(productId);
        }
    }

    @Override
    public Optional<Product> getProduct(UUID id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if(optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            return Optional.of(product);
        }else {
            throw new ProductNotFoundException(id);
        }
    }

    @Override
    public Iterable<Product> findProductsBySearchKey(String searchKey) {
        Iterable<Product> productIterable = productRepository.findBySearchCriteria(searchKey);

        if(!productIterable.iterator().hasNext()){
            throw new ProductsNotFoundException(searchKey);
        }

        return productIterable;
    }

    @Override
    public Iterable<Product> getAllProducts(boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("isDeletedFilter");
        filter.setParameter("deleted", isDeleted);

        Iterable<Product> products =  productRepository.findAll();
        session.disableFilter("isDeletedFilter");

        if(!products.iterator().hasNext()){
            throw new ProductsNotFoundException();
        }

        return products;
    }

}
