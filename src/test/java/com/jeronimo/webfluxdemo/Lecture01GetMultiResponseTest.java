package com.jeronimo.webfluxdemo;

import com.jeronimo.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lecture01GetMultiResponseTest extends BaseTest{

    @Autowired
    private WebClient webClient;

    @Test
    public void fluxTest(){
        Flux<Response> responseFlux = this.webClient
                .get()
                .uri("reactive-math/table/{number}", 5)
                .retrieve()
                .bodyToFlux(Response.class)
                .doOnNext(System.out::println); //Mono<Response>;

        StepVerifier.create(responseFlux)
                .expectNextCount(10)
                .verifyComplete();

    }

    @Test
    public void fluxStreamTest(){
        Flux<Response> responseFlux = this.webClient
                .get()
                .uri("reactive-math/table/{number}/stream", 5)
                .retrieve()
                .bodyToFlux(Response.class)
                .doOnNext(System.out::println); //Mono<Response>;

        StepVerifier.create(responseFlux)
                .expectNextCount(10)
                .verifyComplete();

    }
}
