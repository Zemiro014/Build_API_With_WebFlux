package com.jeronimo.webfluxdemo.webtestclient;

import com.jeronimo.webfluxdemo.controller.ParamsController;
import com.jeronimo.webfluxdemo.controller.ReactiveMathController;
import com.jeronimo.webfluxdemo.dto.Response;
import com.jeronimo.webfluxdemo.service.ReactiveMathService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

/*
* The "WebFluxTest" makes the Autoconfiguration but not scan the all project and doesn't create any Beans.
* If you need some beans it's necessary to specify which beans you need before starting the tests
*
* The "WebFluxTest" allows you creating beans by demand
* */
@WebFluxTest(controllers = {ReactiveMathController.class, ParamsController.class}) // It was specified a bean of ReactiveMathController
public class Lecture02ControllerGetTest {

    @Autowired
    private WebTestClient client; // It was specified a bean of WebTestClient

    @MockBean
    private ReactiveMathService reactiveMathService; // It was specified a bean of ReactiveMathService

    @Test
    public void fluentAssertionTest(){

        Mockito.when(reactiveMathService.findSquare(Mockito.anyInt())).thenReturn(Mono.just(new Response(25)));

        this.client
                .get()
                .uri("/reactive-math/square/{number}", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Response.class)
                .value( response -> Assertions.assertThat(response.getOutput()).isEqualTo(25));

    }

    @Test
    public void fluentAssertionTest_MockReturnEmptyMono(){

        Mockito.when(reactiveMathService.findSquare(Mockito.anyInt())).thenReturn(Mono.empty());

        this.client
                .get()
                .uri("/reactive-math/square/{number}", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Response.class)
                .value( response -> Assertions.assertThat(response.getOutput()).isEqualTo(-1));

    }

    @Test
    public void listResponseTest(){

        Flux<Response> responseFlux = Flux.range(1, 3)
                .map(Response::new);

        Mockito.when(reactiveMathService.multiplicationTable(Mockito.anyInt())).thenReturn(responseFlux);

        this.client
                .get()
                .uri("/reactive-math/table/{number}", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Response.class)
                .hasSize(3);

    }

    @Test
    public void streamingResponseTest(){

        Flux<Response> responseFlux = Flux.range(1, 3)
                .map(Response::new)
                .delayElements(Duration.ofMillis(100));

        Mockito.when(reactiveMathService.multiplicationTable(Mockito.anyInt())).thenReturn(responseFlux);

        this.client
                .get()
                .uri("/reactive-math/table/{number}/stream", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
                .expectBodyList(Response.class)
                .hasSize(3);

    }

    @Test
    public void paramsTest(){
        Map<String, Integer> map = Map.of(
                "count", 10,
                "page", 20
        );

        this.client
                .get()
                .uri(b -> b.path("/jobs/search").query("count={count}&page={page}").build(map))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Integer.class)
                .hasSize(2).contains(10, 20);
    }
}
