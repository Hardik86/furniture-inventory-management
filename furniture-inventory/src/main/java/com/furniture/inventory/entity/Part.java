package com.furniture.inventory.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * Base entity for all parts in the furniture inventory system.
 * Uses inheritance strategy with single table for Inhouse and Outsourced parts.
 *
 * @author Hardik
 * @version 1.0
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "part_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "part")
public abstract class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(name = "inventory_count", nullable = false)
    private int inv;

    @Column(name = "min_inventory", nullable = false)
    private int minInv;

    @Column(name = "max_inventory", nullable = false)
    private int maxInv;

    @ManyToMany(mappedBy = "parts")
    private Set<Product> products = new HashSet<>();

    // Constructors
    public Part() {
        // Default constructor for JPA
    }

    public Part(String name, double price, int inv) {
        this.name = name;
        this.price = price;
        this.inv = inv;
        this.minInv = 0; // Default minimum
        this.maxInv = 100; // Default maximum
    }

    public Part(Long id, String name, double price, int inv) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.inv = inv;
        this.minInv = 0;
        this.maxInv = 100;
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

    public int getMinInv() {
        return minInv;
    }

    public void setMinInv(int minInv) {
        this.minInv = minInv;
    }

    public int getMaxInv() {
        return maxInv;
    }

    public void setMaxInv(int maxInv) {
        this.maxInv = maxInv;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    // Business logic methods
    /**
     * Checks if current inventory is below minimum threshold
     * @return true if inventory needs restocking
     */
    public boolean isInventoryLow() {
        return inv < minInv;
    }

    /**
     * Checks if current inventory exceeds maximum capacity
     * @return true if inventory is overstocked
     */
    public boolean isInventoryOverMax() {
        return inv > maxInv;
    }

    // Standard methods
    @Override
    public String toString() {
        return "Part{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", inv=" + inv +
                ", minInv=" + minInv +
                ", maxInv=" + maxInv +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Part part = (Part) o;
        return Double.compare(price, part.price) == 0 &&
                inv == part.inv &&
                minInv == part.minInv &&
                maxInv == part.maxInv &&
                Objects.equals(id, part.id) &&
                Objects.equals(name, part.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, inv, minInv, maxInv);
    }
}
