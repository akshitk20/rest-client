package com.springboot.restclient.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AstroService {
    private final RestTemplate restTemplate;

    public AstroService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getPeopleInSpace() {
        return restTemplate.getForObject("http://api.open-notify.org/astros.json", String.class);
    }
}
