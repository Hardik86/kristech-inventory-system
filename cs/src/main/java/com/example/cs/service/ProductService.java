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

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void delete(Product product) {
        productRepository.delete(product);
    }

    public boolean purchaseProduct(Long productId) {
        Optional<Product> productOpt = findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            if (product.getInv() > 0) {
                product.setInv(product.getInv() - 1);
                save(product);
                return true;
            }
        }
        return false;
    }

    // REMOVED: The price validation method since we don't need it anymore
    // Product price is now independent of parts costs
    // Customer sets the price manually
}