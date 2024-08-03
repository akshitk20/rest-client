package com.springboot.restclient.service;

import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

public interface AstroInterface {
    @GetExchange("/astros.json")
    Mono<AstroService.AstroResponse> getAstroResponse();

    @GetExchange("/astros.json")
    AstroService.AstroResponse getAstroResponseSync();
}
