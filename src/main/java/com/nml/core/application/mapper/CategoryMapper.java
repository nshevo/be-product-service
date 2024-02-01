package com.nml.core.application.mapper;

import com.nml.core.application.dto.category.CategoryRequest;
import com.nml.core.application.dto.category.CategoryResponse;
import com.nml.core.domain.model.Category;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class CategoryMapper {
    public static CategoryResponse mapCategoryToCategoryResponse(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();

        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        categoryResponse.setDeleted(category.isDeleted());
        categoryResponse.setDescription(category.getDescription());

        return categoryResponse;
    }

    public static Category mapCategoryRequestToCategory(CategoryRequest categoryRequest){
        Category category = Category.builder()
                .id(categoryRequest.getId())
                .deleted(categoryRequest.isDeleted())
                .description(categoryRequest.getDescription())
                .name(categoryRequest.getName())
                .build();

        return category;
    }

    public static Iterable<CategoryResponse> mapCategoriesToCategoryResponses(Iterable<Category> categories) {

        HashSet<CategoryResponse> categoryResponses = new HashSet<>();

        for (Category category : categories) {
            categoryResponses.add(mapCategoryToCategoryResponse(category));
        }

        return categoryResponses;
    }
}
