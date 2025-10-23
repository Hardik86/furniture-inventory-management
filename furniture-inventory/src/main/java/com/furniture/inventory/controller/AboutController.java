package com.furniture.inventory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling the About page functionality.
 * Provides information about the furniture store company.
 *
 * @author Hardik
 * @version 1.0
 */
@Controller
public class AboutController {

    /**
     * Displays the About page with company information
     *
     * @param model Spring model for passing data to view
     * @return about page template name
     */
    @GetMapping("/about")
    public String showAboutPage(Model model) {
        // Company information for the furniture store
        model.addAttribute("companyName", "Premium Furniture Workshop");
        model.addAttribute("foundedYear", "2010");
        model.addAttribute("mission", "To create beautiful, durable furniture that transforms houses into homes");
        model.addAttribute("description",
                "Since 2010, Premium Furniture Workshop has been crafting exceptional furniture " +
                        "using the finest materials and traditional techniques combined with modern design. " +
                        "Our skilled artisans create pieces that are built to last for generations.");
        model.addAttribute("specialties",
                "Custom Woodworking, Upholstery, Furniture Restoration, Bespoke Designs");
        model.addAttribute("address", "123 Artisan Lane, Craftsman District, Woodville, CA 94201");
        model.addAttribute("phone", "(555) 123-4567");
        model.addAttribute("email", "info@premiumfurniture.com");

        return "about";
    }
}