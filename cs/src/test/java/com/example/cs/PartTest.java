package com.example.cs;

import com.example.cs.model.InhousePart;
import com.example.cs.model.Part;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PartTest {

    // TEST 1: Valid inventory within min/max range
    @Test
    public void testPartInventoryWithinRange() {
        // Create part with inventory BETWEEN min and max:

        Part part = new InhousePart("Test CPU", 199.99, 15, 10, 20, 1234);

        // Verify inventory is valid (15 should be between 10-20):

        assertTrue(part.isInventoryValid(),
                "Inventory should be valid when within min/max range");

        // Verify exact values
        assertEquals(15, part.getInv(), "Inventory should be 15");
        assertEquals(10, part.getMinInventory(), "Min inventory should be 10");
        assertEquals(20, part.getMaxInventory(), "Max inventory should be 20");

        // Verify no validation message (null means valid)
        assertNull(part.getInventoryValidationMessage(),
                "Validation message should be null when inventory is valid");
    }

    // TEST 2: Invalid inventory outside min/max bounds:

    @Test
    public void testPartInventoryOutsideRange() {
        // Test BELOW minimum inventory
        Part lowPart = new InhousePart("Test RAM", 89.99, 5, 10, 20, 5678);
        assertFalse(lowPart.isInventoryValid(),
                "Inventory validation should fail when below minimum");
        assertEquals("Inventory 5 is below minimum 10", lowPart.getInventoryValidationMessage(),
                "Should return correct error message for low inventory");

        // Test ABOVE maximum inventory
        Part highPart = new InhousePart("Test SSD", 79.99, 25, 5, 20, 9012);
        assertFalse(highPart.isInventoryValid(),
                "Inventory validation should fail when above maximum");
        assertEquals("Inventory 25 is above maximum 20", highPart.getInventoryValidationMessage(),
                "Should return correct error message for high inventory");
    }

    // TEST 3: Boundary testing (edge cases):

    @Test
    public void testPartInventoryBoundaryValues() {
        // Test AT minimum boundary:

        Part atMinPart = new InhousePart("Min Boundary", 99.99, 10, 10, 20, 1111);
        assertTrue(atMinPart.isInventoryValid(),
                "Inventory should be valid when exactly at minimum");

        // Test AT maximum boundary:

        Part atMaxPart = new InhousePart("Max Boundary", 99.99, 20, 10, 20, 2222);
        assertTrue(atMaxPart.isInventoryValid(),
                "Inventory should be valid when exactly at maximum");
    }

    // TEST 4: Multipack functionality:

    @Test
    public void testMultipackFunctionality() {
        Part part = new InhousePart("Test Part", 49.99, 10, 5, 20, 1111);

        // Test setting multipack quantity:

        part.setMultipackQuantity(3);
        assertEquals(3, part.getMultipackQuantity(),
                "Multipack quantity should be set correctly");

        // Test multipack detection:

        assertTrue(part.isMultipack(),
                "Part should be identified as multipack when quantity > 1");

        // Test single item (not multipack)
        Part singlePart = new InhousePart("Single Part", 29.99, 8, 5, 15, 3333);
        singlePart.setMultipackQuantity(1);
        assertFalse(singlePart.isMultipack(),
                "Part should not be multipack when quantity = 1");
    }

    // TEST 5: Part constructor and default values:

    @Test
    public void testPartConstructorAndDefaults() {
        Part part = new InhousePart("New Part", 149.99, 12, 3, 25, 4444);

        // Test constructor sets values correctly
        assertEquals("New Part", part.getName());
        assertEquals(149.99, part.getPrice(), 0.001); // Delta for double comparison
        assertEquals(12, part.getInv());
        assertEquals(3, part.getMinInventory());
        assertEquals(25, part.getMaxInventory());

        // Test default multipack quantity:

        assertEquals(1, part.getMultipackQuantity(),
                "New parts should have default multipack quantity of 1");
    }
}