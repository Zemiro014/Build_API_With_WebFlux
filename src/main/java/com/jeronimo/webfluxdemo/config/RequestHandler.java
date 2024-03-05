package com.jeronimo.webfluxdemo.config;

import com.jeronimo.webfluxdemo.dto.MultiplyRequestDto;
import com.jeronimo.webfluxdemo.dto.Response;
import com.jeronimo.webfluxdemo.service.ReactiveMathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RequestHandler {

    @Autowired
    private ReactiveMathService mathService;

    public Mono<ServerResponse> squareHandler(ServerRequest serverRequest){
        int input = Integer.valueOf(serverRequest.pathVariable("input"));
        Mono<Response> responseMono = this.mathService.findSquare(input);
        return ServerResponse.ok().body(responseMono, Response.class);
    }


    public Mono<ServerResponse> tableHandler(ServerRequest serverRequest){
        int input = Integer.valueOf(serverRequest.pathVariable("input"));
        Flux<Response> responseFlux = this.mathService.multiplicationTable(input);
        return ServerResponse.ok().body(responseFlux, Response.class);
    }

    public Mono<ServerResponse> tableStreamHandler(ServerRequest serverRequest){
        int input = Integer.valueOf(serverRequest.pathVariable("input"));
        Flux<Response> responseFlux = this.mathService.multiplicationTable(input);
        return ServerResponse.ok()
            .contentType(MediaType.TEXT_EVENT_STREAM)
            .body(responseFlux, Response.class);
    }

    public Mono<ServerResponse> multiplyHandler(ServerRequest serverRequest){
        Mono<MultiplyRequestDto> requestDtoMono = serverRequest.bodyToMono(MultiplyRequestDto.class);
        Mono<Response> responseMono = this.mathService.multiply(requestDtoMono);
        return ServerResponse.ok()
                .body(responseMono, Response.class);
    }
}
