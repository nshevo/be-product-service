package com.nml.core.application.service.interfaces;

import com.nml.core.application.dto.product.ProductRequest;
import com.nml.core.application.dto.product.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface IProductApplicationService {

    boolean createProduct(ProductRequest productRequest);

    boolean updateProduct(ProductRequest productRequest);

    boolean deleteProduct(UUID productId);

    ProductResponse getProduct(UUID id);

    Iterable<ProductResponse> getAllProducts(boolean isDeleted);

    Iterable<ProductResponse> findProductBySearchKey(String searchKey);

    Iterable<ProductResponse> findProductsInCategoryBySearchKey(UUID categoryId, String searchKey);

}
