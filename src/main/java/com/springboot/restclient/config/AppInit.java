package com.springboot.restclient.config;

import com.springboot.restclient.entity.Customer;
import com.springboot.restclient.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class AppInit {
    @Bean
    public CommandLineRunner initializeDatabase(CustomerRepository repository) {
        return args ->
                repository.count().switchIfEmpty(Mono.just(0L))
                        .flatMapMany(count -> repository.deleteAll()
                                .thenMany(Flux.just(
                                        new Customer(null, "Bruce", "Wayne"),
                                        new Customer(null, "Clark", "Kent"),
                                        new Customer(null, "Diana", "Prince"),
                                        new Customer(null, "Barry", "Allen"),
                                        new Customer(null, "Hal", "Jordan")))
                                .flatMap(repository::save))
                        .subscribe(System.out::println);
    }
}
