package com.springboot.restclient.controller;

import com.springboot.restclient.entity.Customer;
import com.springboot.restclient.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public Flux<Customer> findAll() {
        return customerRepository.findAll();
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<Customer>> findById(@PathVariable Long id) {
        return customerRepository.findById(id)
                .log()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Customer> create(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        "Customer with id %d not found".formatted(id))))
                .flatMap(customerRepository::delete);
    }
}
