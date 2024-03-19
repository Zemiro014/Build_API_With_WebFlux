package com.jeronimo.webfluxdemo.webtestclient;

import com.jeronimo.webfluxdemo.dto.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest // This guy scan the all project and creates the Beans before starting the tests
@AutoConfigureWebTestClient
public class Lecture01SimpleWebTestClientTest {

    @Autowired
    private WebTestClient client;
    @Test
    public void fluxStreamTest(){
        Flux<Response> responseFlux = this.client
                .get()
                .uri("/reactive-math/square/{number}", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Response.class)
                .getResponseBody();

        StepVerifier.create(responseFlux)
                .expectNextMatches(r -> r.getOutput() == 25)
                .verifyComplete();

    }

    @Test
    public void fluentAssertionTest(){
        this.client
                .get()
                .uri("/reactive-math/square/{number}", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Response.class)
                .value( response -> Assertions.assertThat(response.getOutput()).isEqualTo(25))
        ;

    }
}
