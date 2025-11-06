package com.example.cs.controller;

import com.example.cs.model.Product;
import com.example.cs.service.PartService;
import com.example.cs.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private PartService partService;

    // ALL EXISTING MAPPINGS UNCHANGED
    @GetMapping
    public String listProducts(Model model) {
        try {
            model.addAttribute("products", productService.findAll());
        } catch (Exception e) {
            model.addAttribute("products", new ArrayList<Product>());
        }
        return "products";
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        try {
            // Product starts with NO parts selected
            model.addAttribute("product", new Product());
            model.addAttribute("allParts", partService.findAll());
        } catch (Exception e) {
            model.addAttribute("product", new Product());
            model.addAttribute("allParts", new ArrayList<>());
        }
        return "product-form";
    }

    // UPDATED: Removed automatic price validation against parts
    @PostMapping("/add")
    public String addProduct(@RequestParam String name,
                             @RequestParam double price,
                             @RequestParam int inv,
                             @RequestParam int minInventory,
                             @RequestParam int maxInventory,
                             @RequestParam(required = false) List<Long> partIds, // Can be null if no parts selected
                             Model model) {
        try {
            // EXISTING PRICE VALIDATION (price must be > 0)
            if (price <= 0) {
                model.addAttribute("error", "ERROR: Price must be greater than 0");
                model.addAttribute("product", new Product(name, price, inv, minInventory, maxInventory));
                try {
                    model.addAttribute("allParts", partService.findAll());
                } catch (Exception ex) {
                    model.addAttribute("allParts", new ArrayList<>());
                }
                return "product-form";
            }

            // EXISTING INVENTORY VALIDATION
            if (inv < minInventory) {
                model.addAttribute("error", "ERROR: Inventory (" + inv + ") cannot be below minimum (" + minInventory + ")");
                model.addAttribute("product", new Product(name, price, inv, minInventory, maxInventory));
                try {
                    model.addAttribute("allParts", partService.findAll());
                } catch (Exception ex) {
                    model.addAttribute("allParts", new ArrayList<>());
                }
                return "product-form";
            }
            if (inv > maxInventory) {
                model.addAttribute("error", "ERROR: Inventory (" + inv + ") cannot exceed maximum (" + maxInventory + ")");
                model.addAttribute("product", new Product(name, price, inv, minInventory, maxInventory));
                try {
                    model.addAttribute("allParts", partService.findAll());
                } catch (Exception ex) {
                    model.addAttribute("allParts", new ArrayList<>());
                }
                return "product-form";
            }

            // CREATE PRODUCT - Customer sets price independently
            Product product = new Product(name, price, inv, minInventory, maxInventory);

            // ONLY ADD PARTS IF CUSTOMER EXPLICITLY SELECTED THEM
            if (partIds != null && !partIds.isEmpty()) {
                product.setParts(new HashSet<>());
                for (Long partId : partIds) {
                    partService.findById(partId).ifPresent(part -> product.getParts().add(part));
                }
            }
            // If partIds is null, product is created with NO parts (empty set)

            productService.save(product);
            return "redirect:/products";
        } catch (Exception e) {
            model.addAttribute("error", "Error adding product: " + e.getMessage());
            try {
                model.addAttribute("allParts", partService.findAll());
            } catch (Exception ex) {
                model.addAttribute("allParts", new ArrayList<>());
            }
            return "product-form";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Long id, Model model) {
        try {
            Product product = productService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
            model.addAttribute("product", product);
            model.addAttribute("allParts", partService.findAll());
            return "product-form";
        } catch (Exception e) {
            return "redirect:/products";
        }
    }

    // UPDATED: Removed automatic price validation against parts
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id,
                                @RequestParam String name,
                                @RequestParam double price,
                                @RequestParam int inv,
                                @RequestParam int minInventory,
                                @RequestParam int maxInventory,
                                @RequestParam(required = false) List<Long> partIds, // Can be null if no parts selected
                                Model model) {
        try {
            // EXISTING PRICE VALIDATION
            if (price <= 0) {
                model.addAttribute("error", "ERROR: Price must be greater than 0");
                try {
                    Product existingProduct = productService.findById(id).orElse(new Product());
                    model.addAttribute("product", existingProduct);
                    model.addAttribute("allParts", partService.findAll());
                } catch (Exception ex) {
                    model.addAttribute("product", new Product());
                    model.addAttribute("allParts", new ArrayList<>());
                }
                return "product-form";
            }

            // EXISTING INVENTORY VALIDATION
            if (inv < minInventory) {
                model.addAttribute("error", "ERROR: Inventory (" + inv + ") cannot be below minimum (" + minInventory + ")");
                try {
                    Product existingProduct = productService.findById(id).orElse(new Product());
                    model.addAttribute("product", existingProduct);
                    model.addAttribute("allParts", partService.findAll());
                } catch (Exception ex) {
                    model.addAttribute("product", new Product());
                    model.addAttribute("allParts", new ArrayList<>());
                }
                return "product-form";
            }
            if (inv > maxInventory) {
                model.addAttribute("error", "ERROR: Inventory (" + inv + ") cannot exceed maximum (" + maxInventory + ")");
                try {
                    Product existingProduct = productService.findById(id).orElse(new Product());
                    model.addAttribute("product", existingProduct);
                    model.addAttribute("allParts", partService.findAll());
                } catch (Exception ex) {
                    model.addAttribute("product", new Product());
                    model.addAttribute("allParts", new ArrayList<>());
                }
                return "product-form";
            }

            Product existingProduct = productService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

            // Update the existing product:
            existingProduct.setName(name);
            existingProduct.setPrice(price); // Customer sets price independently
            existingProduct.setInv(inv);
            existingProduct.setMinInventory(minInventory);
            existingProduct.setMaxInventory(maxInventory);

            // UPDATE PARTS - Only use what customer explicitly selected
            if (partIds != null) {
                existingProduct.getParts().clear();
                for (Long partId : partIds) {
                    partService.findById(partId).ifPresent(part -> existingProduct.getParts().add(part));
                }
            } else {
                // If partIds is null, clear all parts (customer wants no parts)
                existingProduct.getParts().clear();
            }

            productService.save(existingProduct);

            return "redirect:/products";
        } catch (Exception e) {
            model.addAttribute("error", "Error updating product: " + e.getMessage());
            try {
                model.addAttribute("allParts", partService.findAll());
                model.addAttribute("product", productService.findById(id).orElse(new Product()));
            } catch (Exception ex) {
                model.addAttribute("allParts", new ArrayList<>());
                model.addAttribute("product", new Product());
            }
            return "product-form";
        }
    }

    // DELETE MAPPING UNCHANGED
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        try {
            Product product = productService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
            productService.delete(product);
            return "redirect:/products?message=Product deleted successfully";
        } catch (Exception e) {
            return "redirect:/products?error=Error deleting product";
        }
    }

    // BUY MAPPING UNCHANGED
    @PostMapping("/buy/{id}")
    public String buyProduct(@PathVariable("id") Long id, Model model) {
        try {
            boolean success = productService.purchaseProduct(id);
            if (success) {
                model.addAttribute("message", "Product purchased successfully!");
            } else {
                model.addAttribute("error", "Product out of stock!");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error purchasing product: " + e.getMessage());
        }

        try {
            model.addAttribute("products", productService.findAll());
        } catch (Exception e) {
            model.addAttribute("products", new ArrayList<Product>());
        }
        return "products";
    }
}