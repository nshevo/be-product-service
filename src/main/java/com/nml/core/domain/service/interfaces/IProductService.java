package com.nml.core.domain.service.interfaces;


import com.nml.core.domain.model.Product;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public interface IProductService
{
    boolean createProduct (Product product);
    boolean updateProduct (Product product);
    boolean deleteProduct (UUID productId);

    Iterable<Product> findProductsBySearchKey(String searchKey);

    Optional<Product> getProduct(UUID id);
    Iterable<Product> getAllProducts(boolean isDeleted);
}
