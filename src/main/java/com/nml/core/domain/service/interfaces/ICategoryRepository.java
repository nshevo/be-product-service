package com.nml.core.domain.service.interfaces;

import com.nml.core.domain.model.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ICategoryRepository extends CrudRepository<Category, UUID> {

    Iterable<Category> findByNameIgnoreCaseContaining(String searchKey);

    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE %:key% OR LOWER(c.description) LIKE %:key%")
    Iterable<Category> findBySearchCriteria(@Param("key") String key);

}