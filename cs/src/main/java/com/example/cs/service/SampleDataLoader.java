package com.example.cs.service;

import com.example.cs.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.HashSet;

@Component
public class SampleDataLoader implements CommandLineRunner {
    @Autowired private PartService partService;
    @Autowired private ProductService productService;

    @Override
    public void run(String... args) {
        if (partService.findAll().isEmpty() && productService.findAll().isEmpty()) {
            // Create parts
            Part processor = new InhousePart("Intel i7 Processor", 299.99, 25, 5, 50, 1234);
            Part ram = new InhousePart("16GB DDR4 RAM", 89.99, 40, 10, 100, 5678);
            Part ssd = new InhousePart("1TB SSD", 79.99, 35, 5, 80, 9012);
            Part motherboard = new InhousePart("Gaming Motherboard", 199.99, 15, 3, 30, 3456);
            Part gpu = new InhousePart("RTX 4060 GPU", 399.99, 12, 2, 25, 7890);

            partService.save(processor);
            partService.save(ram);
            partService.save(ssd);
            partService.save(motherboard);
            partService.save(gpu);

            // Create products
            Product gamingPc = new Product("Gaming Desktop Pro", 1299.99, 10, 2, 20);
            gamingPc.setParts(new HashSet<>(Arrays.asList(processor, ram, ssd, motherboard, gpu)));

            Product officePc = new Product("Office Workstation", 699.99, 15, 5, 30);
            officePc.setParts(new HashSet<>(Arrays.asList(processor, ram, ssd)));

            Product laptop = new Product("UltraThin Laptop", 899.99, 8, 3, 15);
            laptop.setParts(new HashSet<>(Arrays.asList(processor, ram, ssd)));

            Product server = new Product("Enterprise Server", 2499.99, 5, 1, 10);
            server.setParts(new HashSet<>(Arrays.asList(processor, ram, ssd, motherboard)));

            Product mediaPc = new Product("Media Center PC", 599.99, 12, 4, 25);
            mediaPc.setParts(new HashSet<>(Arrays.asList(ram, ssd, motherboard)));

            productService.save(gamingPc);
            productService.save(officePc);
            productService.save(laptop);
            productService.save(server);
            productService.save(mediaPc);
        }
    }
}