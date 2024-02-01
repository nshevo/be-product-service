package com.nml.core.application.dto.product;

import com.nml.core.domain.model.Category;
import jakarta.persistence.Basic;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @Basic
    private UUID id;

    @NotBlank(message = "Product name is required")
    private String name;

    @Positive(message = "Price of the product is incorrect")
    private double price;

    @PositiveOrZero(message = "Product quantity is missing")
    private int quantity;

    @NotBlank(message = "Product description is required")
    private String description;

    @Basic
    private String details;

    @NotBlank(message = "Product image is missing")
    private String imagelink;

    @Basic(optional = false)
    private Collection<Category> categories = new HashSet<>();

    @Basic
    private boolean deleted;

}
