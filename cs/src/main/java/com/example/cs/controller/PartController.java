package com.example.cs.controller;

import com.example.cs.model.InhousePart;
import com.example.cs.model.OutsourcedPart;
import com.example.cs.model.Part;
import com.example.cs.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/parts")
public class PartController {

    @Autowired
    private PartService partService;

    // ALL EXISTING MAPPINGS UNCHANGED
    @GetMapping
    public String listParts(Model model) {
        try {
            model.addAttribute("parts", partService.findAll());
        } catch (Exception e) {
            model.addAttribute("parts", new ArrayList<Part>());
        }
        return "parts";
    }

    @GetMapping("/chooseType")
    public String choosePartType() {
        return "part-type-select";
    }

    @GetMapping("/add/inhouse")
    public String showAddInhouseForm(Model model) {
        model.addAttribute("part", new InhousePart());
        return "part-form-inhouse";
    }

    @GetMapping("/add/outsourced")
    public String showAddOutsourcedForm(Model model) {
        model.addAttribute("part", new OutsourcedPart());
        return "part-form-outsourced";
    }

    // UPDATED: Added multipack and packSize parameters
    @PostMapping("/add/inhouse")
    public String addInhousePart(@RequestParam String name,
                                 @RequestParam double price,
                                 @RequestParam int inv,
                                 @RequestParam int minInventory,
                                 @RequestParam int maxInventory,
                                 @RequestParam int machineId,
                                 @RequestParam(defaultValue = "false") boolean multipack, // NEW
                                 @RequestParam(defaultValue = "1") int packSize, // NEW
                                 Model model) {
        try {
            // EXISTING VALIDATION STAYS EXACTLY THE SAME
            if (price <= 0) {
                model.addAttribute("error", "ERROR: Price must be greater than 0");
                InhousePart part = new InhousePart(name, price, inv, minInventory, maxInventory, machineId);
                part.setMultipack(multipack);
                part.setPackSize(packSize);
                model.addAttribute("part", part);
                return "part-form-inhouse";
            }

            if (inv < minInventory) {
                model.addAttribute("error", "ERROR: Inventory (" + inv + ") cannot be below minimum (" + minInventory + ")");
                InhousePart part = new InhousePart(name, price, inv, minInventory, maxInventory, machineId);
                part.setMultipack(multipack);
                part.setPackSize(packSize);
                model.addAttribute("part", part);
                return "part-form-inhouse";
            }
            if (inv > maxInventory) {
                model.addAttribute("error", "ERROR: Inventory (" + inv + ") cannot exceed maximum (" + maxInventory + ")");
                InhousePart part = new InhousePart(name, price, inv, minInventory, maxInventory, machineId);
                part.setMultipack(multipack);
                part.setPackSize(packSize);
                model.addAttribute("part", part);
                return "part-form-inhouse";
            }

            InhousePart part = new InhousePart(name, price, inv, minInventory, maxInventory, machineId);
            part.setMultipack(multipack); // SET MULTIPACK
            part.setPackSize(packSize);   // SET PACK SIZE
            partService.save(part);
            return "redirect:/parts";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            InhousePart part = new InhousePart(name, price, inv, minInventory, maxInventory, machineId);
            part.setMultipack(multipack);
            part.setPackSize(packSize);
            model.addAttribute("part", part);
            return "part-form-inhouse";
        }
    }

    // UPDATED: Added multipack and packSize parameters
    @PostMapping("/add/outsourced")
    public String addOutsourcedPart(@RequestParam String name,
                                    @RequestParam double price,
                                    @RequestParam int inv,
                                    @RequestParam int minInventory,
                                    @RequestParam int maxInventory,
                                    @RequestParam String companyName,
                                    @RequestParam(defaultValue = "false") boolean multipack, // NEW
                                    @RequestParam(defaultValue = "1") int packSize, // NEW
                                    Model model) {
        try {
            // EXISTING VALIDATION STAYS EXACTLY THE SAME
            if (price <= 0) {
                model.addAttribute("error", "ERROR: Price must be greater than 0");
                OutsourcedPart part = new OutsourcedPart(name, price, inv, minInventory, maxInventory, companyName);
                part.setMultipack(multipack);
                part.setPackSize(packSize);
                model.addAttribute("part", part);
                return "part-form-outsourced";
            }

            if (inv < minInventory) {
                model.addAttribute("error", "ERROR: Inventory (" + inv + ") cannot be below minimum (" + minInventory + ")");
                OutsourcedPart part = new OutsourcedPart(name, price, inv, minInventory, maxInventory, companyName);
                part.setMultipack(multipack);
                part.setPackSize(packSize);
                model.addAttribute("part", part);
                return "part-form-outsourced";
            }
            if (inv > maxInventory) {
                model.addAttribute("error", "ERROR: Inventory (" + inv + ") cannot exceed maximum (" + maxInventory + ")");
                OutsourcedPart part = new OutsourcedPart(name, price, inv, minInventory, maxInventory, companyName);
                part.setMultipack(multipack);
                part.setPackSize(packSize);
                model.addAttribute("part", part);
                return "part-form-outsourced";
            }

            OutsourcedPart part = new OutsourcedPart(name, price, inv, minInventory, maxInventory, companyName);
            part.setMultipack(multipack); // SET MULTIPACK
            part.setPackSize(packSize);   // SET PACK SIZE
            partService.save(part);
            return "redirect:/parts";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            OutsourcedPart part = new OutsourcedPart(name, price, inv, minInventory, maxInventory, companyName);
            part.setMultipack(multipack);
            part.setPackSize(packSize);
            model.addAttribute("part", part);
            return "part-form-outsourced";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditPartForm(@PathVariable("id") Long id, Model model) {
        try {
            Part part = partService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid part ID"));
            model.addAttribute("part", part);

            if (part instanceof InhousePart) {
                return "part-form-inhouse";
            } else {
                return "part-form-outsourced";
            }
        } catch (Exception e) {
            return "redirect:/parts";
        }
    }

    // UPDATED: Added multipack and packSize parameters
    @PostMapping("/update/{id}")
    public String updatePart(@PathVariable("id") Long id,
                             @RequestParam String name,
                             @RequestParam double price,
                             @RequestParam int inv,
                             @RequestParam int minInventory,
                             @RequestParam int maxInventory,
                             @RequestParam(required = false) Integer machineId,
                             @RequestParam(required = false) String companyName,
                             @RequestParam(defaultValue = "false") boolean multipack, // NEW
                             @RequestParam(defaultValue = "1") int packSize, // NEW
                             Model model) {
        try {
            // EXISTING VALIDATION STAYS EXACTLY THE SAME
            if (price <= 0) {
                model.addAttribute("error", "ERROR: Price must be greater than 0");
                Part part = partService.findById(id).orElse(new InhousePart());
                model.addAttribute("part", part);
                return (part instanceof InhousePart) ? "part-form-inhouse" : "part-form-outsourced";
            }

            if (inv < minInventory) {
                model.addAttribute("error", "ERROR: Inventory (" + inv + ") cannot be below minimum (" + minInventory + ")");
                Part part = partService.findById(id).orElse(new InhousePart());
                model.addAttribute("part", part);
                return (part instanceof InhousePart) ? "part-form-inhouse" : "part-form-outsourced";
            }
            if (inv > maxInventory) {
                model.addAttribute("error", "ERROR: Inventory (" + inv + ") cannot exceed maximum (" + maxInventory + ")");
                Part part = partService.findById(id).orElse(new InhousePart());
                model.addAttribute("part", part);
                return (part instanceof InhousePart) ? "part-form-inhouse" : "part-form-outsourced";
            }

            Part existingPart = partService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid part ID"));

            // Update existing fields (UNCHANGED)
            existingPart.setName(name);
            existingPart.setPrice(price);
            existingPart.setInv(inv);
            existingPart.setMinInventory(minInventory);
            existingPart.setMaxInventory(maxInventory);

            // Update multipack fields (NEW)
            existingPart.setMultipack(multipack);
            existingPart.setPackSize(packSize);

            // Update type-specific fields (UNCHANGED)
            if (existingPart instanceof InhousePart && machineId != null) {
                ((InhousePart) existingPart).setMachineId(machineId);
            } else if (existingPart instanceof OutsourcedPart && companyName != null) {
                ((OutsourcedPart) existingPart).setCompanyName(companyName);
            }

            partService.save(existingPart);
            return "redirect:/parts";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            try {
                Part part = partService.findById(id).orElse(new InhousePart());
                model.addAttribute("part", part);
                return (part instanceof InhousePart) ? "part-form-inhouse" : "part-form-outsourced";
            } catch (Exception ex) {
                return "redirect:/parts";
            }
        }
    }

    // DELETE MAPPING - COMPLETELY UNCHANGED
    @GetMapping("/delete/{id}")
    public String deletePart(@PathVariable("id") Long id) {
        try {
            Part part = partService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid part ID"));

            // Check if part is used in any products:
            if (part.getProducts() != null && !part.getProducts().isEmpty()) {
                return "redirect:/parts?error=Part is used in products and cannot be deleted";
            }

            partService.delete(part);

            return "redirect:/parts?message=Part deleted successfully";

        } catch (Exception e) {
            return "redirect:/parts?error=Error deleting part";
        }
    }
}