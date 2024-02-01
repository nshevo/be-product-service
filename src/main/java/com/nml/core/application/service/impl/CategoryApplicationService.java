package com.nml.core.application.service.impl;

import com.nml.core.application.dto.category.CategoryRequest;
import com.nml.core.application.dto.category.CategoryResponse;
import com.nml.core.application.dto.product.ProductResponse;
import com.nml.core.application.mapper.CategoryMapper;
import com.nml.core.application.mapper.ProductMapper;
import com.nml.core.application.service.exceptions.CategoryApplicationServiceException;
import com.nml.core.application.service.interfaces.ICategoryApplicationService;
import com.nml.core.domain.model.Category;
import com.nml.core.domain.model.Product;
import com.nml.core.domain.service.interfaces.ICategoryService;
import com.nml.core.domain.exception.category.CategoryAlreadyDeletedException;
import com.nml.core.domain.exception.category.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryApplicationService implements ICategoryApplicationService {

    @Autowired
    private final ICategoryService categoryService;

    public CategoryApplicationService(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public boolean createCategory(CategoryRequest categoryRequest) {
        Category category = CategoryMapper.mapCategoryRequestToCategory(categoryRequest);
        return categoryService.createCategory(category);
    }

    @Override
    public boolean updateCategory(CategoryRequest categoryRequest) {
        Category category = CategoryMapper.mapCategoryRequestToCategory(categoryRequest);

        try{
            return categoryService.updateCategory(category);
        }catch (CategoryNotFoundException ex) {
            throw new CategoryApplicationServiceException("Error while updating category");
        }
    }

    @Override
    public boolean deleteCategory(UUID categoryId) {
        try {
            return categoryService.deleteCategory(categoryId);
        } catch (CategoryAlreadyDeletedException | CategoryNotFoundException ex) {
            throw new CategoryApplicationServiceException("Error while deleting category");
        }
    }

    @Override
    public CategoryResponse getCategory(UUID id) {
        Optional<Category> optionalCategory = null;

        try{
            optionalCategory = categoryService.getCategory(id);
        }catch (CategoryNotFoundException e){
            throw new CategoryApplicationServiceException("Error while getting category");
        }

        if(optionalCategory.isPresent()){
            return CategoryMapper.mapCategoryToCategoryResponse(optionalCategory.get());
        }

        return null;
    }

    @Override
    public Iterable<CategoryResponse> getAllCategories(boolean isDeleted) {
        Iterable<Category> categories;

        try{
            categories = categoryService.getAllCategories(isDeleted);
        }catch (RuntimeException e){
            throw new CategoryApplicationServiceException("Error while getting all categories");
        }

        return CategoryMapper.mapCategoriesToCategoryResponses(categories);
    }

    @Override
    public Iterable<CategoryResponse> findCategoryBySearchKey(String searchKey) {
        Iterable<Category> categories;

        try{
            categories = categoryService.findCategoriesBySearchKey(searchKey);
        }catch (RuntimeException e){
            throw new CategoryApplicationServiceException("Error while finding categories by search key");
        }

        return CategoryMapper.mapCategoriesToCategoryResponses(categories);
    }

    @Override
    public Iterable<ProductResponse> getProductsByCategoryId(UUID categoryId) {
        Iterable<Product> products = null;

        try{
            products = categoryService.getProductsByCategoryId(categoryId);
        }catch (RuntimeException e){
            throw new CategoryApplicationServiceException("Error while getting products by category");
        }

        Iterable<ProductResponse> productResponses = ProductMapper.mapProductsToProductResponses(products);
        return productResponses;
    }

}
