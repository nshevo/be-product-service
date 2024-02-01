package com.nml.core.domain.exception.product;

public class ProductsNotFoundException extends RuntimeException {

    public ProductsNotFoundException() {
        super("Could not find any products");
    }

    public ProductsNotFoundException(String searchKey) {
        super("Could not find any products with searchKey: " + searchKey);
    }
}
