package com.springboot.restclient.controller;

import com.springboot.restclient.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private DatabaseClient databaseClient;

    @BeforeEach
    void setUp() {
        var statements = List.of(
                """
                DROP TABLE IF EXISTS customer;
                CREATE TABLE customer(
                    id long generated always as identity primary key,
                    first_name VARCHAR(100) NOT NULL,
                    last_name VARCHAR(100) NOT NULL
                );
                INSERT INTO customer (first_name, last_name) VALUES ('Bruce', 'Wayne');
                INSERT INTO customer (first_name, last_name) VALUES ('Clark', 'Kent');
                INSERT INTO customer (first_name, last_name) VALUES ('Diana', 'Prince');
                INSERT INTO customer (first_name, last_name) VALUES ('Barry', 'Allen');
                INSERT INTO customer (first_name, last_name) VALUES ('Hal', 'Jordan');
                """
        );
        statements.forEach(it -> databaseClient.sql(it)
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete());
    }

    private List<Long> getIds() {
        return databaseClient.sql("select id from customer")
                .map(row -> row.get("id", Long.class))
                .all()
                .collectList()
                .block();
    }

    @Test
    void findAll() {
        client.get()
                .uri("/customers")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Customer.class)
                .hasSize(5);
    }

    @Test
    void findById() {
        getIds().forEach(id ->
                client.get()
                        .uri("/customers/%d".formatted(id))
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody(Customer.class)
                        .value(customer -> assertEquals(id, customer.id())));
    }

    @Test
    void create() {
        Customer customer = new Customer(null, "John", "Stewart");
        client.post()
                .uri("/customers")
                .bodyValue(customer)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Customer.class)
                .value(c -> assertEquals("John", c.firstName()));
    }

    @Test
    void delete() {
        getIds().forEach(id ->
                client.delete()
                        .uri("/customers/%d".formatted(id))
                        .exchange()
                        .expectStatus().isNoContent());
    }

    @Test
    void deleteNotFound() {
        client.delete()
                .uri("/customers/999")
                .exchange()
                .expectStatus().isNotFound();
    }
}
