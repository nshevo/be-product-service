package com.nml.core.application.dto.product;

import com.nml.core.domain.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse implements Serializable {
    private UUID id;
    private String name;
    private double price;
    private int quantity;
    private String description;
    private String details;
    private String imagelink;
    private Collection<Category> categories = new HashSet<>();
    private boolean deleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductResponse that = (ProductResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price) &&
                Objects.equals(quantity, that.quantity) && Objects.equals(description, that.description) &&
                Objects.equals(details, that.details) && Objects.equals(imagelink, that.imagelink) &&
                Objects.equals(categories, that.categories) && Objects.equals(deleted, that.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, quantity, description, details, imagelink, categories, deleted);
    }
}
