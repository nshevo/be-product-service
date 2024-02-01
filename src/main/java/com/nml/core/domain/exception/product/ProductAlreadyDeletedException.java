package com.nml.core.domain.exception.product;

import java.util.UUID;

public class ProductAlreadyDeletedException extends RuntimeException {

    public ProductAlreadyDeletedException(UUID id) {
        super("Could not delete product with id " + id + ", as it is already been deleted.");
    }
}