package com.example.cs.controller;

import com.example.cs.service.PartService;
import com.example.cs.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class MainController {

    @Autowired
    private PartService partService;

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String index(Model model) {
        try {
            model.addAttribute("parts", partService.findAll());
        } catch (Exception e) {
            model.addAttribute("parts", new ArrayList<>());
        }

        try {
            model.addAttribute("products", productService.findAll());
        } catch (Exception e) {
            model.addAttribute("products", new ArrayList<>());
        }

        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}