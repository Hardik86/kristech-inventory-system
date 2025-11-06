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

    @GetMapping
    public String listProducts(Model model) {
        try {
            model.addAttribute("products", productService.findAll());
        } catch (Exception e) {
            model.addAttribute("products", new ArrayList<Product>());
        }
        return "products";
    }

    // ADD THIS GET MAPPING:
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        try {
            model.addAttribute("product", new Product());
            model.addAttribute("allParts", partService.findAll());
        } catch (Exception e) {
            model.addAttribute("product", new Product());
            model.addAttribute("allParts", new ArrayList<>());
        }
        return "product-form";
    }

    @PostMapping("/add")
    public String addProduct(@RequestParam String name,
                             @RequestParam double price,
                             @RequestParam int inv,
                             @RequestParam int minInventory,
                             @RequestParam int maxInventory,
                             @RequestParam(required = false) List<Long> partIds,
                             Model model) {
        try {
            Product product = new Product(name, price, inv, minInventory, maxInventory);

            if (partIds != null) {
                product.setParts(new HashSet<>());
                for (Long partId : partIds) {
                    partService.findById(partId).ifPresent(part -> product.getParts().add(part));
                }
            }

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

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id,
                                @RequestParam String name,
                                @RequestParam double price,
                                @RequestParam int inv,
                                @RequestParam int minInventory,
                                @RequestParam int maxInventory,
                                @RequestParam(required = false) List<Long> partIds,
                                Model model) {
        try {
            Product existingProduct = productService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

            // Update the existing product
            existingProduct.setName(name);
            existingProduct.setPrice(price);
            existingProduct.setInv(inv);
            existingProduct.setMinInventory(minInventory);
            existingProduct.setMaxInventory(maxInventory);

            // Update parts
            if (partIds != null) {
                existingProduct.getParts().clear();
                for (Long partId : partIds) {
                    partService.findById(partId).ifPresent(part -> existingProduct.getParts().add(part));
                }
            } else {
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