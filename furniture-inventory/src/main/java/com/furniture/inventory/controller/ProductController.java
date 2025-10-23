package com.furniture.inventory.controller;

import com.furniture.inventory.entity.Part;
import com.furniture.inventory.entity.Product;
import com.furniture.inventory.repository.PartRepository;
import com.furniture.inventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller for handling Product-related operations including
 * adding, updating, deleting products and the "Buy Now" functionality.
 *
 * @author Hardik
 * @version 1.0
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final PartRepository partRepository;

    @Autowired
    public ProductController(ProductRepository productRepository, PartRepository partRepository) {
        this.productRepository = productRepository;
        this.partRepository = partRepository;
    }

    /**
     * Shows the form for adding a new product
     */
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("availableParts", partRepository.findAll());
        model.addAttribute("title", "Add Product");
        return "product-form";
    }

    /**
     * Handles the submission of new products
     */
    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product, Model model) {
        try {
            // For new products with no parts, reset inventory to 0
            if (product.getParts().isEmpty() && product.getInv() > 0) {
                product.setInv(0);
            }

            // Validate product price against part costs
            if (!product.isPriceValid()) {
                model.addAttribute("error",
                        "Product price must be greater than or equal to the sum of part prices. " +
                                "Current part total: $" + String.format("%.2f", product.getPartsTotalCost()));
                model.addAttribute("availableParts", partRepository.findAll());
                model.addAttribute("title", "Add Product");
                return "product-form";
            }

            productRepository.save(product);
            return "redirect:/mainscreen";

        } catch (Exception e) {
            model.addAttribute("error", "Error saving product: " + e.getMessage());
            model.addAttribute("availableParts", partRepository.findAll());
            model.addAttribute("title", "Add Product");
            return "product-form";
        }
    }

    /**
     * Shows the form for updating an existing product
     */
    @GetMapping("/update/{id}")
    public String showUpdateProductForm(@PathVariable Long id, Model model) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            model.addAttribute("product", product);
            model.addAttribute("availableParts", partRepository.findAll());
            model.addAttribute("title", "Update Product");
            return "product-form";
        } else {
            return "redirect:/mainscreen";
        }
    }

    /**
     * Handles updating existing products
     */
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product updatedProduct, Model model) {
        try {
            Optional<Product> existingProductOptional = productRepository.findById(id);

            if (existingProductOptional.isPresent()) {
                Product existingProduct = existingProductOptional.get();
                int oldInventory = existingProduct.getInv();

                // Update fields
                existingProduct.setName(updatedProduct.getName());
                existingProduct.setPrice(updatedProduct.getPrice());
                existingProduct.setInv(updatedProduct.getInv());
                existingProduct.setParts(updatedProduct.getParts());

                // Validate product price against part costs
                if (!existingProduct.isPriceValid()) {
                    model.addAttribute("error",
                            "Product price must be greater than or equal to the sum of part prices. " +
                                    "Current part total: $" + String.format("%.2f", existingProduct.getPartsTotalCost()));
                    model.addAttribute("availableParts", partRepository.findAll());
                    model.addAttribute("title", "Update Product");
                    return "product-form";
                }

                // Handle inventory changes and part consumption
                handleInventoryChange(existingProduct, oldInventory);

                productRepository.save(existingProduct);
            }

            return "redirect:/mainscreen";

        } catch (Exception e) {
            model.addAttribute("error", "Error updating product: " + e.getMessage());
            model.addAttribute("availableParts", partRepository.findAll());
            model.addAttribute("title", "Update Product");
            return "product-form";
        }
    }

    /**
     * Handles the "Buy Now" functionality - decrements product inventory by 1
     */
    @PostMapping("/buy/{id}")
    public String buyProduct(@PathVariable Long id, Model model) {
        try {
            Optional<Product> productOptional = productRepository.findById(id);

            if (productOptional.isPresent()) {
                Product product = productOptional.get();

                if (product.decrementInventory()) {
                    productRepository.save(product);
                    model.addAttribute("success",
                            "Successfully purchased '" + product.getName() + "'. Inventory updated.");
                } else {
                    model.addAttribute("error",
                            "Cannot purchase '" + product.getName() + "'. Product is out of stock.");
                }
            }

            return "redirect:/mainscreen";

        } catch (Exception e) {
            model.addAttribute("error", "Error processing purchase: " + e.getMessage());
            return "redirect:/mainscreen";
        }
    }

    /**
     * Handles deletion of products
     */
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/mainscreen";
    }

    /**
     * Handles inventory changes and updates part inventory accordingly
     * When product inventory increases, it consumes parts from inventory
     */
    private void handleInventoryChange(Product product, int oldInventory) {
        int inventoryChange = product.getInv() - oldInventory;

        if (inventoryChange > 0) {
            // Product inventory increased - consume parts
            for (Part part : product.getParts()) {
                int newPartInventory = part.getInv() - inventoryChange;

                // Check if part inventory would go below minimum
                if (newPartInventory < part.getMinInv()) {
                    throw new IllegalArgumentException(
                            "Cannot assemble product. Part '" + part.getName() +
                                    "' would have inventory below minimum. Required: " + inventoryChange +
                                    ", Available: " + (part.getInv() - part.getMinInv()));
                }

                part.setInv(newPartInventory);
                partRepository.save(part);
            }
        }
        // Note: Decreasing product inventory (sales) doesn't return parts to inventory
    }
}