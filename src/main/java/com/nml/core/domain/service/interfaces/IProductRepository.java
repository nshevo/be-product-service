package com.nml.core.domain.service.interfaces;

import com.nml.core.domain.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface IProductRepository extends CrudRepository<Product, UUID> {

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE %:key% OR LOWER(p.description) LIKE %:key% OR LOWER(p.details) LIKE %:key%")
    Iterable<Product> findBySearchCriteria(@Param("key") String key);

}


