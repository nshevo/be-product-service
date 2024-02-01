package com.nml.core.application.service.impl;

import com.nml.core.application.dto.product.ProductRequest;
import com.nml.core.application.dto.product.ProductResponse;
import com.nml.core.application.mapper.ProductMapper;
import com.nml.core.application.service.exceptions.ProductApplicationServiceException;
import com.nml.core.application.service.interfaces.IProductApplicationService;
import com.nml.core.domain.model.Category;
import com.nml.core.domain.model.Product;
import com.nml.core.domain.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductApplicationService implements IProductApplicationService {

    @Autowired
    private final IProductService productService;

    public ProductApplicationService(IProductService productService) {
        this.productService = productService;
    }

    @Override
    public boolean createProduct(ProductRequest productRequest) {
        Product product = ProductMapper.mapProductRequestToProduct(productRequest);
        return productService.createProduct(product);
    }

    @Override
    public boolean updateProduct(ProductRequest productRequest) {
        Product product = ProductMapper.mapProductRequestToProduct(productRequest);

        try{
            return productService.updateProduct(product);
        }catch (RuntimeException e){
            throw new ProductApplicationServiceException("Error while updating product");
        }
    }

    @Override
    public boolean deleteProduct(UUID productId) {
        try{
            return productService.deleteProduct(productId);
        }catch (RuntimeException e){
            throw new ProductApplicationServiceException("Error while deleting product");
        }
    }

    @Override
    public ProductResponse getProduct(UUID id) {
        Optional<Product> optionalProduct = null;

        try{
            optionalProduct = productService.getProduct(id);
        }catch (RuntimeException e){
            throw new ProductApplicationServiceException("Error while getting product");
        }

        return ProductMapper.mapProductToProductResponse(optionalProduct.get());
    }

    @Override
    public Iterable<ProductResponse> getAllProducts(boolean isDeleted) {
        Iterable<Product> products = null;

        try{
            products = productService.getAllProducts(isDeleted);
        }catch (RuntimeException e){
            throw new ProductApplicationServiceException("Error while getting products");
        }

        return ProductMapper.mapProductsToProductResponses(products);
    }

    @Override
    public Iterable<ProductResponse> findProductBySearchKey(String searchKey) {
        Iterable<Product> products = tryProducts(searchKey);

        return ProductMapper.mapProductsToProductResponses(products);
    }

    @Override
    public Iterable<ProductResponse> findProductsInCategoryBySearchKey(UUID categoryId, String searchKey) {
        Iterable<Product> products = tryProducts(searchKey);

        HashSet<ProductResponse> productsInCategory = new HashSet<>();
        for(Product product : products){
            for(Category productCategory : product.getCategories()){
                if(productCategory.getId().equals(categoryId)){
                    productsInCategory.add(ProductMapper.mapProductToProductResponse(product));
                }
            }
        }
        return productsInCategory;
    }

    private Iterable<Product> tryProducts(String searchKey){
        try{
            return productService.findProductsBySearchKey(searchKey);
        }catch (RuntimeException e){
            throw new ProductApplicationServiceException("Error while getting products");
        }
    }

}

