package com.nml.core.domain.exception.product;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(UUID id) {
      super("Could not find product with id " + id);
    }

}
