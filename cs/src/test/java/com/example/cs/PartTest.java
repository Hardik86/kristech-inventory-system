package com.example.cs;

import com.example.cs.model.InhousePart;
import com.example.cs.model.Part;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PartTest {

    @Test
    public void testPartInventoryWithinRange() {
        Part part = new InhousePart("Test CPU", 199.99, 15, 10, 20, 1234);
        assertTrue(part.isInventoryValid());
        assertEquals(15, part.getInv());
        assertEquals(10, part.getMinInventory());
        assertEquals(20, part.getMaxInventory());
    }

    @Test
    public void testPartInventoryOutsideRange() {
        Part lowPart = new InhousePart("Test RAM", 89.99, 5, 10, 20, 5678);
        assertFalse(lowPart.isInventoryValid());
        assertEquals("Inventory 5 is below minimum 10", lowPart.getInventoryValidationMessage());

        Part highPart = new InhousePart("Test SSD", 79.99, 25, 5, 20, 9012);
        assertFalse(highPart.isInventoryValid());
        assertEquals("Inventory 25 is above maximum 20", highPart.getInventoryValidationMessage());
    }

    @Test
    public void testMultipackFunctionality() {
        Part part = new InhousePart("Test Part", 49.99, 10, 5, 20, 1111);
        part.setMultipackQuantity(3);
        assertEquals(3, part.getMultipackQuantity());
        assertTrue(part.isMultipack());
    }
}