package com.furniture.inventory.controller;

import com.furniture.inventory.entity.Part;
import com.furniture.inventory.entity.Product;
import com.furniture.inventory.repository.PartRepository;
import com.furniture.inventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Main controller for handling the inventory management application.
 * Provides endpoints for the main screen and basic navigation.
 *
 * @author Hardik
 * @version 1.0
 */
@Controller
public class MainController {

    private final PartRepository partRepository;
    private final ProductRepository productRepository;

    @Autowired
    public MainController(PartRepository partRepository, ProductRepository productRepository) {
        this.partRepository = partRepository;
        this.productRepository = productRepository;
    }

    /**
     * Displays the main inventory management screen
     * Supports filtering for both parts and products
     *
     * @param partFilter optional filter for parts search
     * @param productFilter optional filter for products search
     * @param model Spring model for passing data to view
     * @return main screen template name
     */
    @GetMapping("/mainscreen")
    public String showMainScreen(
            @RequestParam(required = false) String partFilter,
            @RequestParam(required = false) String productFilter,
            Model model) {

        // Handle parts filtering
        List<Part> parts;
        if (partFilter != null && !partFilter.trim().isEmpty()) {
            parts = partRepository.findByNameContainingIgnoreCase(partFilter.trim());
            model.addAttribute("partFilter", partFilter);
        } else {
            parts = partRepository.findAll();
        }

        // Handle products filtering
        List<Product> products;
        if (productFilter != null && !productFilter.trim().isEmpty()) {
            products = productRepository.findByNameContainingIgnoreCase(productFilter.trim());
            model.addAttribute("productFilter", productFilter);
        } else {
            products = productRepository.findAll();
        }

        // Add data to model for Thymeleaf template
        model.addAttribute("parts", parts);
        model.addAttribute("products", products);
        model.addAttribute("shopName", "Premium Furniture Workshop");

        return "mainscreen";
    }

    /**
     * Handles clearing of filters and redirects back to main screen
     *
     * @return redirect to main screen without filters
     */
    @GetMapping("/clearFilters")
    public String clearFilters() {
        return "redirect:/mainscreen";
    }
}