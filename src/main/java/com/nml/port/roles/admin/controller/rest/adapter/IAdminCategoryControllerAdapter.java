package com.nml.port.roles.admin.controller.rest.adapter;

import com.nml.core.application.dto.category.CategoryRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RequestMapping("/admin")
public interface IAdminCategoryControllerAdapter {

    @PostMapping(path = "/category")
    ResponseEntity<String> createCategory(@Valid @RequestBody CategoryRequest categoryRequest);

    @PutMapping(path = "/category")
    ResponseEntity<String> updateCategory(@Valid @RequestBody CategoryRequest categoryRequest);

    @DeleteMapping(path = "/category/{categoryId}")
    ResponseEntity<String> deleteCategory(@Valid @PathVariable UUID categoryId);
}

