package com.furniture.inventory.repository;

import com.furniture.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Product entities with custom query methods.
 * Provides data access operations for furniture products.
 *
 * @author Hardik
 * @version 1.0
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds products by name containing ignore case
     * @param name the name to search for
     * @return list of matching products
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * Finds products that use a specific part
     * @param partId the part ID to search for
     * @return list of products containing the part
     */
    @Query("SELECT p FROM Product p JOIN p.parts part WHERE part.id = :partId")
    List<Product> findProductsByPartId(@Param("partId") Long partId);

    /**
     * Calculates total value of inventory (sum of price * inventory for all products)
     * @return total inventory value
     */
    @Query("SELECT SUM(p.price * p.inv) FROM Product p")
    Double calculateTotalInventoryValue();
}