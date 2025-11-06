package com.example.cs.controller;

import com.example.cs.model.Part;
import com.example.cs.model.Product;
import com.example.cs.service.PartService;
import com.example.cs.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    private ProductService productService;

    @Autowired
    private PartService partService;

    // Show purchase page for a product
    @GetMapping("/product/{id}")
    public String showPurchasePage(@PathVariable("id") Long id, Model model) {
        try {
            Product product = productService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

            // Get all available parts
            List<Part> allParts = partService.findAll();

            model.addAttribute("product", product);
            model.addAttribute("allParts", allParts);
            model.addAttribute("selectedPartIds", new ArrayList<Long>()); // Start with no parts selected

            return "purchase-page";
        } catch (Exception e) {
            return "redirect:/products?error=Product not found";
        }
    }

    // Process the purchase
    @PostMapping("/complete")
    public String completePurchase(@RequestParam Long productId,
                                   @RequestParam(required = false) List<Long> selectedPartIds,
                                   Model model) {
        try {
            Product product = productService.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

            // Check if product is in stock
            if (product.getInv() <= 0) {
                model.addAttribute("error", "Sorry, this product is out of stock!");
                model.addAttribute("products", productService.findAll());
                return "products";
            }

            // Calculate total cost
            double productPrice = product.getPrice();
            double partsTotal = 0.0;
            List<Part> selectedParts = new ArrayList<>();

            if (selectedPartIds != null) {
                for (Long partId : selectedPartIds) {
                    Optional<Part> partOpt = partService.findById(partId);
                    if (partOpt.isPresent()) {
                        Part part = partOpt.get();
                        selectedParts.add(part);
                        partsTotal += part.getPrice();
                    }
                }
            }

            double totalCost = productPrice + partsTotal;

            // Deduct inventory
            product.setInv(product.getInv() - 1);
            productService.save(product);

            // Show purchase confirmation
            model.addAttribute("product", product);
            model.addAttribute("selectedParts", selectedParts);
            model.addAttribute("productPrice", productPrice);
            model.addAttribute("partsTotal", partsTotal);
            model.addAttribute("totalCost", totalCost);

            return "purchase-confirmation";

        } catch (Exception e) {
            model.addAttribute("error", "Error processing purchase: " + e.getMessage());
            model.addAttribute("products", productService.findAll());
            return "products";
        }
    }
}