package com.example.cs;

import com.example.cs.model.InhousePart;
import com.example.cs.model.Part;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PartTest {

    @Test
    void testPartCreation() {
        Part part = new InhousePart("Test Part", 19.99, 50, 10, 100, 123);

        assertEquals("Test Part", part.getName());
        assertEquals(19.99, part.getPrice());
        assertEquals(50, part.getInv());
        assertEquals(10, part.getMinInventory());
        assertEquals(100, part.getMaxInventory());
    }

    @Test
    void testInventoryValidation() {
        Part part = new InhousePart("Test Part", 19.99, 50, 10, 100, 123);

        // Test valid inventory
        assertTrue(part.getInv() >= part.getMinInventory() && part.getInv() <= part.getMaxInventory());

        // Test invalid inventory (below min)
        Part lowInventoryPart = new InhousePart("Low Part", 19.99, 5, 10, 100, 123);
        assertTrue(lowInventoryPart.getInv() < lowInventoryPart.getMinInventory());

        // Test invalid inventory (above max)
        Part highInventoryPart = new InhousePart("High Part", 19.99, 150, 10, 100, 123);
        assertTrue(highInventoryPart.getInv() > highInventoryPart.getMaxInventory());
    }

    @Test
    void testMultipackFunctionality() {
        Part singlePart = new InhousePart("Single Part", 10.00, 50, 10, 100, 123);
        assertEquals(1, singlePart.getPackSize()); // FIXED: getPackSize()
        assertFalse(singlePart.isMultipack());      // FIXED: isMultipack()
        assertEquals(10.00, singlePart.getUnitPrice());

        Part multipackPart = new InhousePart("Multipack Part", 25.00, 20, 5, 50, 123);
        multipackPart.setPackSize(5); // FIXED: setPackSize()
        multipackPart.setMultipack(true); // FIXED: setMultipack()
        assertEquals(5, multipackPart.getPackSize());
        assertTrue(multipackPart.isMultipack());
        assertEquals(5.00, multipackPart.getUnitPrice()); // 25.00 / 5 = 5.00
    }

    @Test
    void testPriceValidation() {
        Part part = new InhousePart("Test Part", 19.99, 50, 10, 100, 123);
        assertTrue(part.getPrice() > 0);

        Part freePart = new InhousePart("Free Part", 0.00, 50, 10, 100, 123);
        assertFalse(freePart.getPrice() > 0);
    }
}