package com.furniture.inventory.repository;

import com.furniture.inventory.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Part entities with custom query methods.
 * Provides data access operations for both Inhouse and Outsourced parts.
 *
 * @author Hardik
 * @version 1.0
 */
@Repository
public interface PartRepository extends JpaRepository<Part, Long> {

    /**
     * Finds parts by name containing ignore case
     * @param name the name to search for
     * @return list of matching parts
     */
    List<Part> findByNameContainingIgnoreCase(String name);

    /**
     * Finds parts with inventory below minimum threshold
     * @return list of parts that need restocking
     */
    @Query("SELECT p FROM Part p WHERE p.inv < p.minInv")
    List<Part> findPartsWithLowInventory();

    /**
     * Finds parts with inventory above maximum capacity
     * @return list of overstocked parts
     */
    @Query("SELECT p FROM Part p WHERE p.inv > p.maxInv")
    List<Part> findPartsWithExcessInventory();

    /**
     * Checks if a part is used in any product
     * @param partId the part ID to check
     * @return true if part is used in any product
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Product pr JOIN pr.parts p WHERE p.id = :partId")
    boolean isPartUsedInProducts(@Param("partId") Long partId);
}