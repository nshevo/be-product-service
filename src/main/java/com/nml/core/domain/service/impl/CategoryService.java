package com.nml.core.domain.service.impl;

import com.nml.core.domain.exception.category.CategoriesNotFoundException;
import com.nml.core.domain.exception.category.repository.CategoryRepositoryException;
import com.nml.core.domain.model.Category;
import com.nml.core.domain.model.Product;
import com.nml.core.domain.service.interfaces.ICategoryRepository;
import com.nml.core.domain.service.interfaces.ICategoryService;
import com.nml.core.domain.exception.category.CategoryAlreadyDeletedException;
import com.nml.core.domain.exception.category.CategoryNotFoundException;
import jakarta.persistence.EntityManager;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private ICategoryRepository categoryRepository;

    @Autowired
    private EntityManager entityManager;

    public boolean saveCategory(Category category){
        try {
            categoryRepository.save(category);
            return true;
        } catch (RuntimeException ex) {
            throw new CategoryRepositoryException("Saving category failed", ex);
        }
    }

    @Override
    public boolean createCategory(Category category) {
        return saveCategory(category);
    }

    @Override
    public boolean updateCategory(Category category) {
        Optional<Category> existingCategoryOptional = categoryRepository.findById(category.getId());

        if (existingCategoryOptional.isPresent()) {
            Category existingCategory = existingCategoryOptional.get();

            existingCategory.setName(category.getName());
            existingCategory.setDescription(category.getDescription());

            return saveCategory(existingCategory);
        } else {
            throw new CategoryNotFoundException(category.getId());
        }
    }

    @Override
    public boolean deleteCategory(UUID categoryId) {
        Optional<Category> categoryToBeDeleted = categoryRepository.findById(categoryId);

        if (categoryToBeDeleted.isPresent()) {
            if (categoryToBeDeleted.get().isDeleted()) {
                throw new CategoryAlreadyDeletedException(categoryId);
            }
            try{
                categoryRepository.delete(categoryToBeDeleted.get());
                return true;
            }catch (RuntimeException ex){
                throw new CategoryRepositoryException("Deleting category failed", ex);
            }
        } else {
            throw new CategoryNotFoundException(categoryId);
        }
    }

    @Override
    public Optional<Category> getCategory(UUID id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            return Optional.of(category);
        } else {
            throw new CategoryNotFoundException(id);
        }
    }

    @Override
    public Iterable<Category> findCategoriesBySearchKey(String searchKey) {
        Iterable<Category> categories = categoryRepository.findBySearchCriteria(searchKey);

        if(!categories.iterator().hasNext()){
            throw new CategoriesNotFoundException(searchKey);
        }

        return categories;
    }

    @Override
    public Iterable<Category> getAllCategories(boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("isDeletedFilter");

        filter.setParameter("deleted", isDeleted);
        Iterable<Category> categories = categoryRepository.findAll();
        session.disableFilter("isDeletedFilter");

        if(!categories.iterator().hasNext()){
            throw new CategoriesNotFoundException();
        }

        return categories;
    }

    @Override
    public Iterable<Product> getProductsByCategoryId(UUID categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            List<Product> productList = new ArrayList<>();

            category.getProducts().forEach(product -> {
                productList.add(product);
            });

            return productList;
        } else {
            throw new CategoryNotFoundException(categoryId);
        }
    }

}
