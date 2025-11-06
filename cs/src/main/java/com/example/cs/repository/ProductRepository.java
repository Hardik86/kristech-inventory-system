package com.example.cs.repository;

import com.example.cs.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT DISTINCT p FROM Product p JOIN p.parts part WHERE LOWER(part.name) LIKE LOWER(CONCAT('%', :partName, '%'))")
    List<Product> findByPartsNameContainingIgnoreCase(@Param("partName") String partName);
}