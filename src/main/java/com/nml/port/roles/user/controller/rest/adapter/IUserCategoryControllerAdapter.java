package com.nml.port.roles.user.controller.rest.adapter;

import com.nml.core.application.dto.category.CategoryResponse;
import com.nml.core.application.dto.product.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Validated
@RequestMapping("/user")
public interface IUserCategoryControllerAdapter {
    ResponseEntity<CategoryResponse> getCategory(@Valid @PathVariable UUID categoryId);
    ResponseEntity<Iterable<CategoryResponse>> getCategories(@Valid @RequestParam(value = "deleted", required = false, defaultValue = "false") boolean deleted);
    ResponseEntity<Iterable<CategoryResponse>> findCategoriesBySearchKey(@Valid @RequestParam String searchKey);
    ResponseEntity<Iterable<ProductResponse>> getProductsByCategory(@Valid @PathVariable UUID categoryId);
}
