package com.springboot.restclient.repository;

import com.springboot.restclient.entity.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Long> {
    Flux<Customer> findByLastName(String lastName);
}
