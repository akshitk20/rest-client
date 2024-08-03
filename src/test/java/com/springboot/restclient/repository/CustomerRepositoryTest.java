package com.springboot.restclient.repository;

import com.springboot.restclient.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

@DataR2dbcTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repository;

    private List<Customer> customers = List.of(
            new Customer(null, "Bruce", "Wayne"),
            new Customer(null, "Clark", "Kent"),
            new Customer(null, "Diana", "Prince"),
            new Customer(null, "Barry", "Allen"),
            new Customer(null, "Hal", "Jordan"));

    @BeforeEach
    void setUp() {
        customers = repository.deleteAll()
                .thenMany(Flux.fromIterable(customers))
                .flatMap(repository::save)
                .collectList().block();
    }

    @Test
    public void fetchAllCustomers() {
        repository.findAll()
                .doOnNext(System.out::println)
                .as(StepVerifier::create)
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    void fetchCustomerById() {
        repository.findById(customers.get(0).id())
                .doOnNext(System.out::println)
                .as(StepVerifier::create)
                .expectNextMatches(customer -> customer.firstName().equals("Bruce"))
                .verifyComplete();
    }

    @Test
    void fetchCustomersByLastName() {
        repository.findByLastName("Allen")
                .doOnNext(System.out::println)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void insertCustomer() {
        Customer newCustomer = new Customer(null, "Billy", "Batson");
        repository.save(newCustomer)
                .doOnNext(System.out::println)
                .as(StepVerifier::create)
                .expectNextMatches(customer -> customer.firstName().equals("Billy"))
                .verifyComplete();
    }

    @Test
    void updateCustomer() {
        Customer updatedCustomer = new Customer(customers.get(0).id(),
                "Damian", "Wayne");
        repository.save(updatedCustomer)
                .doOnNext(System.out::println)
                .as(StepVerifier::create)
                .expectNextMatches(customer -> customer.firstName().equals("Damian"))
                .verifyComplete();
    }

    @Test
    void deleteCustomer() {
        repository.deleteById(customers.get(0).id())
                .doOnNext(System.out::println)
                .as(StepVerifier::create)
                .verifyComplete();
    }

}