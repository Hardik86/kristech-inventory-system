package com.example.cs.service;

import com.example.cs.model.Product;
import com.example.cs.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() { return productRepository.findAll(); }
    public Optional<Product> findById(Long id) { return productRepository.findById(id); }

    public Product save(Product product) {
        // Validate inventory range
        if (product.getInv() < product.getMinInventory()) {
            throw new IllegalArgumentException("Product inventory " + product.getInv() + " is below minimum " + product.getMinInventory());
        }
        if (product.getInv() > product.getMaxInventory()) {
            throw new IllegalArgumentException("Product inventory " + product.getInv() + " is above maximum " + product.getMaxInventory());
        }

        // Validate product price vs parts total
        if (!product.isPriceValid()) {
            throw new IllegalArgumentException("Product price $" + product.getPrice() + " must be >= parts total $" + product.getTotalPartsPrice());
        }

        return productRepository.save(product);
    }

    public void delete(Product product) { productRepository.delete(product); }

    public boolean purchaseProduct(Long productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent() && productOpt.get().getInv() > 0) {
            Product product = productOpt.get();
            product.setInv(product.getInv() - 1);
            productRepository.save(product);
            return true;
        }
        return false;
    }

    public List<Product> findProductsByPartName(String partName) {
        return productRepository.findByPartsNameContainingIgnoreCase(partName);
    }
}