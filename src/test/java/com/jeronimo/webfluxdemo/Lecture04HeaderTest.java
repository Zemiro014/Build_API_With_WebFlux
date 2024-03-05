package com.jeronimo.webfluxdemo;

import com.jeronimo.webfluxdemo.dto.MultiplyRequestDto;
import com.jeronimo.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lecture04HeaderTest extends BaseTest{

    @Autowired
    private WebClient webClient;

    @Test
    public void headerTest(){
        Mono<Response> responseMono = this.webClient
                .post()
                .uri("reactive-math/multiply")
                .bodyValue(buildRequestDto(5, 2))
                .headers(header -> header.set("someKey", "someValue"))
                .retrieve()
                .bodyToMono(Response.class)
                .doOnNext(System.out::println); //Mono<Response>;

        StepVerifier.create(responseMono)
                .expectNextCount(1)
                .verifyComplete();
    }

    private MultiplyRequestDto buildRequestDto(int a, int b){
        MultiplyRequestDto multiplyRequestDto = new MultiplyRequestDto();
        multiplyRequestDto.setFirst(a);
        multiplyRequestDto.setSecond(b);
        return multiplyRequestDto;
    }
}
