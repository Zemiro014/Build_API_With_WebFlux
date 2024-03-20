package com.jeronimo.webfluxdemo.webtestclient;

import com.jeronimo.webfluxdemo.config.RequestHandler;
import com.jeronimo.webfluxdemo.config.RouterConfig;
import com.jeronimo.webfluxdemo.dto.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@WebFluxTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = RouterConfig.class)
public class Lecture05RouterFunctionTest {

    private WebTestClient client;

    @Autowired
    private ApplicationContext context;
    @MockBean
    private RequestHandler handler;

    @BeforeAll
    public void setClient(){
        this.client = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    public void test(){

        Mockito.when(handler.squareHandler(Mockito.any())).thenReturn(ServerResponse.ok().bodyValue(new Response(22)));

        this.client
                .get()
                .uri("/router/square/{input}", 15)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Response.class)
                .value(response -> Assertions.assertThat(response.getOutput()).isEqualTo(22));
    }
}
