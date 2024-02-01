package com.nml.core.domain.exception.product.repository;

public class ProductRepositoryException extends RuntimeException {
    public ProductRepositoryException(String message, RuntimeException e) {
        super(message, e);
    }

    public ProductRepositoryException(String message) {
        super(message);
    }
}
