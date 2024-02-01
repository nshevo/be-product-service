package com.nml.core.domain.exception.category;

import java.util.UUID;

public class CategoryAlreadyDeletedException extends RuntimeException {

    public CategoryAlreadyDeletedException(UUID categoryId) {
        super("Category already deleted with id: " + categoryId);
    }
}
