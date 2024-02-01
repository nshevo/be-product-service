package com.nml.port.roles.user.controller.rest;

import com.nml.core.application.dto.product.ProductResponse;
import com.nml.core.application.service.interfaces.IProductApplicationService;
import com.nml.port.roles.user.controller.rest.adapter.IUserProductControllerAdapter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.UUID;

@RequestMapping("/user")
@RestController
public class UserProductController implements IUserProductControllerAdapter {

    @Autowired
    private IProductApplicationService productApplicationService;

    @GetMapping("/product/{productId}")
    @Override
    public ResponseEntity<ProductResponse> getProduct(@Valid @PathVariable UUID productId) {
        if(productId == null) {
            return ResponseEntity.badRequest().build();
        }

        ProductResponse productResponse = productApplicationService.getProduct(productId);

        if(productResponse != null) {
            return ResponseEntity.ok(productResponse);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/products")
    @Override
    public ResponseEntity<Iterable<ProductResponse>> getProducts(@Valid @RequestParam(value="deleted", required = false, defaultValue = "false") boolean deleted) {
        Iterable<ProductResponse> products = productApplicationService.getAllProducts(deleted);

        if(products != null){
            Iterator<ProductResponse> iterator = products.iterator();

            if (iterator.hasNext()) {
                return ResponseEntity.ok(products);
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/findProducts")
    @Override
    public ResponseEntity<Iterable<ProductResponse>> findProductBySearchKey(@Valid @RequestParam String searchKey){
        if(searchKey == null) {
            return ResponseEntity.badRequest().build();
        }

        Iterable<ProductResponse> products = productApplicationService.findProductBySearchKey(searchKey);

        if(products != null){
            Iterator<ProductResponse> iterator = products.iterator();
            if (iterator.hasNext()) {
                return ResponseEntity.ok(products);
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/findProductsInCategory")
    @Override
    public ResponseEntity<Iterable<ProductResponse>> findProductInCategoryBySearchKey(@Valid @RequestParam UUID categoryId, @Valid @RequestParam String searchKey){
        if(searchKey == null || categoryId == null) {
            return ResponseEntity.badRequest().build();
        }

        Iterable<ProductResponse> products = productApplicationService.findProductsInCategoryBySearchKey(categoryId, searchKey);

        if(products != null){
            Iterator<ProductResponse> iterator = products.iterator();
            if (iterator.hasNext()) {
                return ResponseEntity.ok(products);
            }
        }

        return ResponseEntity.badRequest().build();
    }

}
