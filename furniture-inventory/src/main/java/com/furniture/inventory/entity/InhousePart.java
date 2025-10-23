package com.furniture.inventory.entity;

import jakarta.persistence.*;

/**
 * Represents parts manufactured in-house for furniture production.
 * Includes machine ID for tracking production equipment.
 *
 * @author Hardik
 * @version 1.0
 */
@Entity
@DiscriminatorValue("INHOUSE")
public class InhousePart extends Part {

    @Column(name = "machine_id")
    private int partId;

    // Constructors
    public InhousePart() {
        super();
    }

    public InhousePart(String name, double price, int inv, int partId) {
        super(name, price, inv);
        this.partId = partId;
    }

    public InhousePart(Long id, String name, double price, int inv, int partId) {
        super(id, name, price, inv);
        this.partId = partId;
    }

    // Getters and Setters
    public int getPartId() {
        return partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    @Override
    public String toString() {
        return "InhousePart{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", price=" + getPrice() +
                ", inv=" + getInv() +
                ", minInv=" + getMinInv() +
                ", maxInv=" + getMaxInv() +
                ", partId=" + partId +
                '}';
    }
}