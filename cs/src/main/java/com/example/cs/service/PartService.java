package com.example.cs.service;

import com.example.cs.model.Part;
import com.example.cs.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartService {
    @Autowired
    private PartRepository partRepository;

    public List<Part> findAll() {

        return partRepository.findAll();
    }

    public Optional<Part> findById(Long id) {

        return partRepository.findById(id);
    }

    // UPDATE THE SAVE METHOD IN PartService:
    public Part save(Part part) {
        // Validate inventory range
        if (part.getInv() < part.getMinInventory()) {
            throw new IllegalArgumentException("Inventory " + part.getInv() + " is below minimum " + part.getMinInventory());
        }
        if (part.getInv() > part.getMaxInventory()) {
            throw new IllegalArgumentException("Inventory " + part.getInv() + " is above maximum " + part.getMaxInventory());
        }

        // Multipack logic - only for new parts with same name:

        if (part.getId() == null) {
            Optional<Part> existing = partRepository.findByName(part.getName());
            if (existing.isPresent()) {
                Part existingPart = existing.get();
                existingPart.setMultipackQuantity(existingPart.getMultipackQuantity() + 1);
                existingPart.setInv(existingPart.getInv() + part.getInv()); // Add inventory
                return partRepository.save(existingPart);
            }
        }
        return partRepository.save(part);
    }

    public void delete(Part part) {

        partRepository.delete(part);
    }

    public List<Part> findByNameContaining(String name) {

        return partRepository.findByNameContainingIgnoreCase(name);
    }
}