package com.nml.core.application.service.interfaces;

import com.nml.core.application.dto.category.CategoryRequest;
import com.nml.core.application.dto.category.CategoryResponse;
import com.nml.core.application.dto.product.ProductResponse;
import jakarta.validation.Valid;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Service
public interface ICategoryApplicationService {

    boolean createCategory(CategoryRequest categoryRequest);

    boolean updateCategory(CategoryRequest categoryRequest);

    boolean deleteCategory(UUID categoryId);

    CategoryResponse getCategory(UUID id);

    Iterable<CategoryResponse> getAllCategories(boolean isDeleted);

    Iterable<CategoryResponse> findCategoryBySearchKey(String searchKey);

    Iterable<ProductResponse> getProductsByCategoryId(UUID categoryId);

}
