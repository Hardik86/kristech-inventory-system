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

    public Part save(Part part) {
        return partRepository.save(part);
    }

    public void delete(Part part) {
        partRepository.delete(part);
    }

    // FIXED: Updated method names to match new multipack fields
    public Part updatePart(Long id, Part partDetails) {
        Part existingPart = partRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Part not found"));

        existingPart.setName(partDetails.getName());
        existingPart.setPrice(partDetails.getPrice());
        existingPart.setInv(partDetails.getInv());
        existingPart.setMinInventory(partDetails.getMinInventory());
        existingPart.setMaxInventory(partDetails.getMaxInventory());
        existingPart.setMultipack(partDetails.isMultipack()); // FIXED: isMultipack()
        existingPart.setPackSize(partDetails.getPackSize());  // FIXED: getPackSize()

        return partRepository.save(existingPart);
    }

    public List<Part> findByNameContaining(String name) {
        return partRepository.findByNameContainingIgnoreCase(name);
    }
}