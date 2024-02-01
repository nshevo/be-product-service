package com.nml.port.roles.admin.controller.rest;

import com.nml.core.application.dto.product.ProductRequest;
import com.nml.core.application.service.interfaces.IProductApplicationService;
import com.nml.port.roles.admin.controller.rest.adapter.IAdminProductControllerAdapter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/admin")
@RestController
public class AdminProductController implements IAdminProductControllerAdapter {

    private final IProductApplicationService productApplicationService;

    @Autowired
    public AdminProductController(IProductApplicationService productApplicationService) {
        this.productApplicationService = productApplicationService;
    }

    @PostMapping(path = "/product")
    @Override
    public ResponseEntity<String> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        if(productRequest == null){
            return ResponseEntity.badRequest().body("Product could not be created");
        }

        if(productApplicationService.createProduct(productRequest)){
            return ResponseEntity.status(HttpStatus.CREATED).body("Product successfully created");
        }

        return ResponseEntity.badRequest().body("Product could not be created");
    }

    @PutMapping(path = "/product")
    @Override
    public ResponseEntity<String> updateProduct(@Valid @RequestBody ProductRequest productRequest) {
        if(productRequest == null)
            return ResponseEntity.badRequest().body("Product could not be updated");

        if(productApplicationService.updateProduct(productRequest)){
            return ResponseEntity.ok().body("Product successfully updated");
        }

        return ResponseEntity.badRequest().body("Product could not be updated");
    }

    @DeleteMapping(path = "/product/{productId}")
    @Override
    public ResponseEntity<String> deleteProduct(@Valid @PathVariable UUID productId) {
        if(productId == null)
            return ResponseEntity.badRequest().body("Product could not be deleted");

        if(productApplicationService.deleteProduct(productId))
            return ResponseEntity.ok().body("Product successfully deleted");

        return ResponseEntity.badRequest().body("Product could not be deleted");

    }
}
