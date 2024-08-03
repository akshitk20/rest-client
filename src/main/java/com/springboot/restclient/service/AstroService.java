package com.springboot.restclient.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class AstroService {
    /*
    * Records
    * Java 16
    * Immutable data holders
    * auto generated equals() hashcode() toString()
    * primary constructor appears before the record body in {}
    * property getters match the names, as in message() number()
    * */
    public record AstroResponse(String message, int number, List<People> people) {
        public record People(String craft, String name) {}
    }
    private final RestTemplate restTemplate;

    private final WebClient client = WebClient.create("http://api.open-notify.org");

    private final RestClient restClient = RestClient.create("http://api.open-notify.org");

    public AstroService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getPeopleInSpace() {
        return restTemplate.getForObject("http://api.open-notify.org/astros.json", String.class);
    }

    public AstroResponse getAstroResponseSync() {
        return restTemplate.getForObject("http://api.open-notify.org/astros.json", AstroResponse.class);
    }

    public Mono<AstroResponse> getAstroResponseAsync() {
        return client.get()
                .uri("/astros.json")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve() // sets up the request
                .bodyToMono(AstroResponse.class) // decode the body to target type
                .log();
    }

    public AstroResponse getAstroResponseRestClient() {
        return restClient.get()
                .uri("/astros.json")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(AstroResponse.class);
    }
}
