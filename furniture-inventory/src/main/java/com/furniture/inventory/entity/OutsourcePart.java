package com.furniture.inventory.entity;

import jakarta.persistence.*;

/**
 * Represents parts sourced from external vendors for furniture assembly.
 * Tracks the company name for supplier management.
 *
 * @author Hardik
 * @version 1.0
 */
@Entity
@DiscriminatorValue("OUTSOURCED")
public class OutsourcedPart extends Part {

    @Column(name = "company_name")
    private String companyName;

    // Constructors
    public OutsourcedPart() {
        super();
    }

    public OutsourcedPart(String name, double price, int inv, String companyName) {
        super(name, price, inv);
        this.companyName = companyName;
    }

    public OutsourcedPart(Long id, String name, double price, int inv, String companyName) {
        super(id, name, price, inv);
        this.companyName = companyName;
    }

    // Getters and Setters
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "OutsourcedPart{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", price=" + getPrice() +
                ", inv=" + getInv() +
                ", minInv=" + getMinInv() +
                ", maxInv=" + getMaxInv() +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}