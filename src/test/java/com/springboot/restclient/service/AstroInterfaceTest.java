package com.springboot.restclient.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AstroInterfaceTest {

    @Autowired
    AstroInterface astroInterface;

    @Test
    void getAstroResponse() {
        AstroService.AstroResponse response = astroInterface.getAstroResponse().block(Duration.ofSeconds(2));
        assertNotNull(response);
        System.out.println(response);
    }

    @Test
    void getAstroResponseSync() {
        AstroService.AstroResponse response = astroInterface.getAstroResponseSync();
        assertNotNull(response);
        System.out.println(response);
    }

}