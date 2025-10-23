package com.furniture.inventory.controller;

import com.furniture.inventory.entity.InhousePart;
import com.furniture.inventory.entity.OutsourcedPart;
import com.furniture.inventory.entity.Part;
import com.furniture.inventory.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller for handling Part-related operations including
 * adding, updating, and deleting both Inhouse and Outsourced parts.
 *
 * @author Hardik
 * @version 1.0
 */
@Controller
@RequestMapping("/parts")
public class PartController {

    private final PartRepository partRepository;

    @Autowired
    public PartController(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    /**
     * Shows the form for adding a new Inhouse part
     */
    @GetMapping("/inhouse/add")
    public String showAddInhousePartForm(Model model) {
        model.addAttribute("part", new InhousePart());
        model.addAttribute("partType", "inhouse");
        model.addAttribute("title", "Add Inhouse Part");
        return "part-form";
    }

    /**
     * Shows the form for adding a new Outsourced part
     */
    @GetMapping("/outsourced/add")
    public String showAddOutsourcedPartForm(Model model) {
        model.addAttribute("part", new OutsourcedPart());
        model.addAttribute("partType", "outsourced");
        model.addAttribute("title", "Add Outsourced Part");
        return "part-form";
    }

    /**
     * Handles the submission of new parts (both Inhouse and Outsourced)
     */
    @PostMapping("/add")
    public String addPart(@ModelAttribute Part part, Model model) {
        try {
            // Set default min/max values if not provided
            if (part.getMinInv() == 0 && part.getMaxInv() == 0) {
                part.setMinInv(0);
                part.setMaxInv(100);
            }

            // Validate inventory against min/max
            if (part.getInv() < part.getMinInv()) {
                model.addAttribute("error", "Inventory cannot be less than minimum inventory: " + part.getMinInv());
                return "part-form";
            }

            if (part.getInv() > part.getMaxInv()) {
                model.addAttribute("error", "Inventory cannot exceed maximum inventory: " + part.getMaxInv());
                return "part-form";
            }

            partRepository.save(part);
            return "redirect:/mainscreen";

        } catch (Exception e) {
            model.addAttribute("error", "Error saving part: " + e.getMessage());
            return "part-form";
        }
    }

    /**
     * Shows the form for updating an existing part
     */
    @GetMapping("/update/{id}")
    public String showUpdatePartForm(@PathVariable Long id, Model model) {
        Optional<Part> partOptional = partRepository.findById(id);

        if (partOptional.isPresent()) {
            Part part = partOptional.get();
            model.addAttribute("part", part);

            if (part instanceof InhousePart) {
                model.addAttribute("partType", "inhouse");
                model.addAttribute("title", "Update Inhouse Part");
            } else if (part instanceof OutsourcedPart) {
                model.addAttribute("partType", "outsourced");
                model.addAttribute("title", "Update Outsourced Part");
            }

            return "part-form";
        } else {
            return "redirect:/mainscreen";
        }
    }

    /**
     * Handles updating existing parts
     */
    @PostMapping("/update/{id}")
    public String updatePart(@PathVariable Long id, @ModelAttribute Part updatedPart, Model model) {
        try {
            Optional<Part> existingPartOptional = partRepository.findById(id);

            if (existingPartOptional.isPresent()) {
                Part existingPart = existingPartOptional.get();

                // Update common fields
                existingPart.setName(updatedPart.getName());
                existingPart.setPrice(updatedPart.getPrice());
                existingPart.setInv(updatedPart.getInv());
                existingPart.setMinInv(updatedPart.getMinInv());
                existingPart.setMaxInv(updatedPart.getMaxInv());

                // Update type-specific fields
                if (existingPart instanceof InhousePart && updatedPart instanceof InhousePart) {
                    ((InhousePart) existingPart).setPartId(((InhousePart) updatedPart).getPartId());
                } else if (existingPart instanceof OutsourcedPart && updatedPart instanceof OutsourcedPart) {
                    ((OutsourcedPart) existingPart).setCompanyName(((OutsourcedPart) updatedPart).getCompanyName());
                }

                // Validate inventory against min/max
                if (existingPart.getInv() < existingPart.getMinInv()) {
                    model.addAttribute("error", "Inventory cannot be less than minimum inventory: " + existingPart.getMinInv());
                    model.addAttribute("part", existingPart);
                    return "part-form";
                }

                if (existingPart.getInv() > existingPart.getMaxInv()) {
                    model.addAttribute("error", "Inventory cannot exceed maximum inventory: " + existingPart.getMaxInv());
                    model.addAttribute("part", existingPart);
                    return "part-form";
                }

                partRepository.save(existingPart);
            }

            return "redirect:/mainscreen";

        } catch (Exception e) {
            model.addAttribute("error", "Error updating part: " + e.getMessage());
            model.addAttribute("part", updatedPart);
            return "part-form";
        }
    }

    /**
     * Handles deletion of parts with validation
     */
    @GetMapping("/delete/{id}")
    public String deletePart(@PathVariable Long id, Model model) {
        try {
            Optional<Part> partOptional = partRepository.findById(id);

            if (partOptional.isPresent()) {
                Part part = partOptional.get();

                // Check if part is used in any products
                if (!part.getProducts().isEmpty()) {
                    model.addAttribute("error",
                            "Cannot delete part '" + part.getName() + "' because it is used in products. " +
                                    "You can set its inventory to 0 instead.");
                    return "redirect:/mainscreen"; // We'll handle this error display later
                }

                partRepository.deleteById(id);
            }

            return "redirect:/mainscreen";

        } catch (Exception e) {
            model.addAttribute("error", "Error deleting part: " + e.getMessage());
            return "redirect:/mainscreen";
        }
    }
}
