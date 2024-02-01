package com.nml.core.domain.model;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.action.internal.OrphanRemovalAction;
import org.hibernate.annotations.*;

import java.util.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Table(name = "products")
@FilterDef(name = "isDeletedFilter", parameters = @ParamDef(name = "deleted", type = boolean.class))
@Filter(name = "isDeletedFilter", condition = "is_deleted = :deleted")
@SQLDelete(sql = "UPDATE products SET deleted = true WHERE id=?")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private double price;

    private int quantity;

    private String description;

    private String details;

    private String imagelink;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Collection<Category> categories = new HashSet<>();

    @Column(name = "is_deleted")
    private boolean deleted = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Double.compare(product.getPrice(), getPrice()) == 0 &&
                getQuantity() == product.getQuantity() &&
                isDeleted() == product.isDeleted() &&
                Objects.equals(getId(), product.getId()) &&
                Objects.equals(getName(), product.getName()) &&
                Objects.equals(getDescription(), product.getDescription()) &&
                Objects.equals(getDetails(), product.getDetails()) &&
                Objects.equals(getImagelink(), product.getImagelink()) &&
                Objects.equals(getCategories(), product.getCategories());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                ", details='" + details + '\'' +
                ", imagelink='" + imagelink + '\'' +
                ", categories=" + categories +
                ", deleted=" + deleted +
                '}';
    }

    public void addCategory(Category category) {
        if(categories == null){
            categories = new HashSet<>();
        }
        categories.add(category);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
    }

    public void removeAllCategories() {
        this.categories.clear();
    }

    public void addCategories(Set<Category> categories) {
        this.categories.addAll(categories);
    }

    public void removeCategories(Set<Category> categories) {
        this.categories.removeAll(categories);
    }

}
