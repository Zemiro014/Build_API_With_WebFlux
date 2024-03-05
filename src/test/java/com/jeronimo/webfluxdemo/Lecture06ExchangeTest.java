package com.jeronimo.webfluxdemo;

import com.jeronimo.webfluxdemo.dto.InputFailedValidationResponse;
import com.jeronimo.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lecture06ExchangeTest extends BaseTest {

    @Autowired
    private WebClient webClient;

    // exhange = retrieve + additional info http status code
    @Test
    public void badRequestTest(){
        Mono<Object> responseMono = this.webClient
            .get()
            .uri("reactive-math/square/{number}/throw", 5)
            .exchangeToMono(this::exchange)
            .doOnNext(System.out::println)
            .doOnError(err -> System.out.println(err.getMessage()));//Mono<Response>

        StepVerifier.create(responseMono)
            .expectNextCount(1)
            .verifyComplete();
    }

    private Mono<Object> exchange(ClientResponse cr){
        System.out.println("Status : "+cr.statusCode().value());
        if(cr.statusCode().value() == 400)
            return cr.bodyToMono(InputFailedValidationResponse.class);
        else
            return cr.bodyToMono(Response.class);
    }
}
