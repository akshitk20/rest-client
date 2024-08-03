package com.springboot.restclient.config;

import com.springboot.restclient.service.AstroInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class AppConfig {

    @Bean
    public AstroInterface astroInterface() {
        WebClient client = WebClient.create("http://api.open-notify.org");
        WebClientAdapter webClientAdapter = WebClientAdapter.create(client);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return httpServiceProxyFactory.createClient(AstroInterface.class);
    }
}
