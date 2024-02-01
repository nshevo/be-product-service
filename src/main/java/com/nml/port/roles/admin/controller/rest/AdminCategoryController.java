package com.nml.port.roles.admin.controller.rest;

import com.nml.core.application.dto.category.CategoryRequest;
import com.nml.core.application.service.interfaces.ICategoryApplicationService;
import com.nml.port.roles.admin.controller.rest.adapter.IAdminCategoryControllerAdapter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/admin")
@RestController
public class AdminCategoryController implements IAdminCategoryControllerAdapter {

    private final ICategoryApplicationService categoryApplicationService;

    @Autowired
    public AdminCategoryController(ICategoryApplicationService categoryApplicationService) {
        this.categoryApplicationService = categoryApplicationService;
    }

    @PostMapping("/category")
    @Override
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        if(categoryRequest == null)
            return ResponseEntity.badRequest().body("Category could not be created");

        if(categoryApplicationService.createCategory(categoryRequest)){
            return ResponseEntity.status(HttpStatus.CREATED).body("Category successfully created");
        }

        return ResponseEntity.badRequest().body("Category could not be created");
    }

    @PutMapping("/category")
    @Override
    public ResponseEntity<String> updateCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        if(categoryRequest == null)
            return ResponseEntity.badRequest().body("Category could not be updated");

        if (categoryApplicationService.updateCategory(categoryRequest)) {
            return ResponseEntity.ok().body("Category successfully updated");
        } else {
            return ResponseEntity.badRequest().body("Category could not be updated");
        }
    }

    @DeleteMapping("/category/{categoryId}")
    @Override
    public ResponseEntity<String> deleteCategory(@Valid @PathVariable UUID categoryId) {

        if (categoryApplicationService.deleteCategory(categoryId)) {
            return ResponseEntity.ok().body("Category successfully deleted");
        } else {
            return ResponseEntity.badRequest().body("Category could not be deleted");
        }
    }

}
