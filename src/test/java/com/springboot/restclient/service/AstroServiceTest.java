package com.springboot.restclient.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AstroServiceTest {

    @Autowired
    private AstroService astroService;

    @Test
    void getPeopleInSpace() {
        String peopleInSpace = astroService.getPeopleInSpace();
        assertNotNull(peopleInSpace);
        assertTrue(peopleInSpace.contains("people"));
        System.out.println(peopleInSpace);
    }

    @Test
    void getAstroResponseSync() {
        AstroService.AstroResponse response = astroService.getAstroResponseSync();
        assertNotNull(response);
        assertEquals("success", response.message());
        assertTrue(response.number() >= 0);
        assertEquals(response.number(), response.people().size());
        System.out.println(response);
    }

}