package com.example.cs.repository;

import com.example.cs.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PartRepository extends JpaRepository<Part, Long> {
    Optional<Part> findByName(String name);
    List<Part> findByNameContainingIgnoreCase(String name);
}