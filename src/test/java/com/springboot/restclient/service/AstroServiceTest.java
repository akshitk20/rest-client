package com.springboot.restclient.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.time.Duration;

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
    // subscribes to mono onSubscribe
    // request for all signals in case of mono only 1 request
    // onNext is invoked when signal came through(to get data) onNext
    // onComplete() -> process is completed
    @Test
    void getAstroResponseAsync() {
        AstroService.AstroResponse response = astroService.getAstroResponseAsync()
                .block(Duration.ofSeconds(2));
        assertNotNull(response);
        assertEquals("success", response.message());
        assertTrue(response.number() >= 0);
        assertEquals(response.number(), response.people().size());
        System.out.println(response);
    }

    // no block method
    @Test
    void getAstroResponseAsyncStepVerifier() {
        astroService.getAstroResponseAsync() // subscribe to mono
                .as(StepVerifier::create)
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals("success", response.message());
                    assertTrue(response.number() >= 0);
                    assertEquals(response.number(), response.people().size());
                    System.out.println(response);
                })
                .verifyComplete(); // analogous to onComplete()
    }
}