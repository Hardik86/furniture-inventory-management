package com.furniture.inventory.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * Represents a furniture product assembled from various parts.
 * Tracks product information and associated parts for assembly.
 *
 * @author Hardik
 * @version 1.0
 */
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(name = "inventory_count", nullable = false)
    private int inv;

    @ManyToMany
    @JoinTable(
            name = "product_parts",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "part_id")
    )
    private Set<Part> parts = new HashSet<>();

    // Constructors
    public Product() {
        // Default constructor for JPA
    }

    public Product(String name, double price, int inv) {
        this.name = name;
        this.price = price;
        this.inv = inv;
    }

    public Product(Long id, String name, double price, int inv) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.inv = inv;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getInv() {
        return inv;
    }

    public void setInv(int inv) {
        this.inv = inv;
    }

    public Set<Part> getParts() {
        return parts;
    }

    public void setParts(Set<Part> parts) {
        this.parts = parts;
    }

    // Business logic methods
    /**
     * Calculates the total cost of all parts in this product
     * @return sum of all part prices
     */
    public double getPartsTotalCost() {
        return parts.stream()
                .mapToDouble(Part::getPrice)
                .sum();
    }

    /**
     * Checks if product price is sufficient to cover part costs
     * @return true if product price >= sum of part prices
     */
    public boolean isPriceValid() {
        return price >= getPartsTotalCost();
    }

    /**
     * Decrements inventory by one (for "Buy Now" feature)
     * @return true if successful, false if inventory already zero
     */
    public boolean decrementInventory() {
        if (inv > 0) {
            inv--;
            return true;
        }
        return false;
    }

    // Standard methods
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", inv=" + inv +
                ", partsCount=" + parts.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(price, product.price) == 0 &&
                inv == product.inv &&
                Objects.equals(id, product.id) &&
                Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, inv);
    }
}