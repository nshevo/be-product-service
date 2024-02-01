package com.nml.port.roles.admin.controller.rest.adapter;

import com.nml.core.application.dto.product.ProductRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RequestMapping("/admin")
public interface IAdminProductControllerAdapter {

    @PostMapping(path = "/product")
    ResponseEntity<String> createProduct(@Valid @RequestBody ProductRequest productRequest);

    @PutMapping(path = "/product")
    ResponseEntity<String> updateProduct(@Valid @RequestBody ProductRequest productRequest);

    @DeleteMapping(path = "/product/{productId}")
    ResponseEntity<String> deleteProduct(@Valid @PathVariable UUID productId);
}
