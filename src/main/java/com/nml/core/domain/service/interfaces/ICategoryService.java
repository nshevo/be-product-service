package com.nml.core.domain.service.interfaces;

import com.nml.core.domain.model.Category;
import com.nml.core.domain.model.Product;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public interface ICategoryService {

    boolean createCategory(Category category);
    boolean updateCategory(Category category);
    boolean deleteCategory(UUID categoryId);

    Iterable<Category> findCategoriesBySearchKey(String searchKey);

    Optional<Category> getCategory(UUID categoryId);
    Iterable<Category> getAllCategories(boolean isDeleted);

    Iterable<Product> getProductsByCategoryId(UUID categoryId);
}
