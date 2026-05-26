package com.example.demo;

import com.example.demo.dto.BfhlRequest;
import com.example.demo.dto.BfhlResponse;
import com.example.demo.service.BfhlService;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class BfhlServiceTest {

    private final BfhlService service = new BfhlService();

    @Test
    public void testOddEvenSeparation() {
        BfhlRequest request = new BfhlRequest();
        request.setData(Arrays.asList("2", "4", "5", "92"));
        
        BfhlResponse response = service.processData(request);
        
        assertEquals(Arrays.asList("5"), response.getOdd_numbers());
        assertEquals(Arrays.asList("2", "4", "92"), response.getEven_numbers());
    }

    @Test
    public void testSum() {
        BfhlRequest request = new BfhlRequest();
        request.setData(Arrays.asList("2", "4", "5", "92"));
        
        BfhlResponse response = service.processData(request);
        
        assertEquals("103", response.getSum());
    }

    @Test
    public void testSpecialCharacters() {
        BfhlRequest request = new BfhlRequest();
        request.setData(Arrays.asList("&", "-", "*", "a", "1"));
        
        BfhlResponse response = service.processData(request);
        
        assertEquals(Arrays.asList("&", "-", "*"), response.getSpecial_characters());
    }

    @Test
    public void testConcatString() {
        BfhlRequest request = new BfhlRequest();
        request.setData(Arrays.asList("a", "y", "b"));
        
        BfhlResponse response = service.processData(request);
        
        assertEquals("ByA", response.getConcat_string());
    }

    @Test
    public void testAlphabetsUppercase() {
        BfhlRequest request = new BfhlRequest();
        request.setData(Arrays.asList("a", "Y", "b"));
        
        BfhlResponse response = service.processData(request);
        
        assertEquals(Arrays.asList("A", "Y", "B"), response.getAlphabets());
    }

    @Test
    public void testCompleteExample() {
        BfhlRequest request = new BfhlRequest();
        request.setData(Arrays.asList("2", "a", "y", "4", "&", "-", "*", "5", "92", "b"));
        
        BfhlResponse response = service.processData(request);
        
        assertTrue(response.isIs_success());
        assertEquals("lovekush_nagda_08022003", response.getUser_id());
        assertEquals("lovekushnagda231252@acropolis.in", response.getEmail());
        assertEquals("0827CY231039", response.getRoll_number());
        assertEquals(Arrays.asList("5"), response.getOdd_numbers());
        assertEquals(Arrays.asList("2", "4", "92"), response.getEven_numbers());
        assertEquals(Arrays.asList("A", "Y", "B"), response.getAlphabets());
        assertEquals(Arrays.asList("&", "-", "*"), response.getSpecial_characters());
        assertEquals("103", response.getSum());
        assertEquals("ByA", response.getConcat_string());
    }
}
