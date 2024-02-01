package com.nml.core.domain.exception.category;

public class CategoriesNotFoundException extends RuntimeException{
    public CategoriesNotFoundException() {
        super("Could not find any categories");
    }

    public CategoriesNotFoundException(String searchKey) {
        super("Could not find any categories with searchKey: " + searchKey);
    }
}
