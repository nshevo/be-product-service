package com.nml.core.domain.exception.category.repository;

public class CategoryRepositoryException extends RuntimeException {
    public CategoryRepositoryException(String message, RuntimeException e) {
        super(message, e);
    }

    public CategoryRepositoryException(String message) {
        super(message);
    }
}
