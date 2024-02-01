package com.nml.port.roles.user.controller.rest;

import com.nml.core.application.dto.category.CategoryResponse;
import com.nml.core.application.dto.product.ProductResponse;
import com.nml.core.application.service.interfaces.ICategoryApplicationService;
import com.nml.port.roles.user.controller.rest.adapter.IUserCategoryControllerAdapter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.UUID;

@RequestMapping("/user")
@RestController
public class UserCategoryController implements IUserCategoryControllerAdapter {

    @Autowired
    private ICategoryApplicationService categoryApplicationService;

    @GetMapping("/category/{categoryId}")
    @Override
    public ResponseEntity<CategoryResponse> getCategory(@Valid @PathVariable UUID categoryId) {
        if(categoryId == null) {
            return ResponseEntity.badRequest().build();
        }

        CategoryResponse categoryResponse = categoryApplicationService.getCategory(categoryId);

        if (categoryResponse != null ) {
            return ResponseEntity.ok(categoryResponse);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/categories")
    @Override
    public ResponseEntity<Iterable<CategoryResponse>> getCategories(@Valid @RequestParam(value = "deleted", required = false, defaultValue = "false") boolean deleted) {
        Iterable<CategoryResponse> categories = categoryApplicationService.getAllCategories(deleted);

        if (categories != null) {
            Iterator<CategoryResponse> iterator = categories.iterator();

            if (iterator.hasNext()) {
                return ResponseEntity.ok(categories);
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/findCategories")
    @Override
    public ResponseEntity<Iterable<CategoryResponse>> findCategoriesBySearchKey(@Valid @RequestParam String searchKey) {
        if(searchKey == null  || searchKey.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Iterable<CategoryResponse> categories = categoryApplicationService.findCategoryBySearchKey(searchKey);

        if (categories != null) {
            Iterator<CategoryResponse> iterator = categories.iterator();
            if (iterator.hasNext()) {
                return ResponseEntity.ok(categories);
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/category/{categoryId}/products")
    @Override
    public ResponseEntity<Iterable<ProductResponse>> getProductsByCategory(@Valid @PathVariable UUID categoryId) {
        if(categoryId == null) {
            return ResponseEntity.badRequest().build();
        }

        Iterable<ProductResponse> products = categoryApplicationService.getProductsByCategoryId(categoryId);

        if (products != null) {
            Iterator<ProductResponse> iterator = products.iterator();
            if (iterator.hasNext()) {
                return ResponseEntity.ok(products);
            }
        }

        return ResponseEntity.badRequest().build();
    }

}
