package com.jeronimo.webfluxdemo.config;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@Service
public class CalculatorHandler {

    // creating multiple handlers intentionally

    // calculator/{a}/{b}
    public Mono<ServerResponse> additionalHandler(ServerRequest serverRequest){
        return process(serverRequest, (a, b) -> ServerResponse.ok().bodyValue(a+" + "+b+" = "+(a+b)));
    }

    public Mono<ServerResponse> subtractionHandler(ServerRequest serverRequest){
        return process(serverRequest, (a, b) -> ServerResponse.ok().bodyValue(a+" - "+b+" = "+(a-b)));
    }

    public Mono<ServerResponse> multiplicationHandler(ServerRequest serverRequest){
        return process(serverRequest, (a, b) ->  b != 0 ? ServerResponse.ok().bodyValue(a+" * "+b+" = "+(a*b))
                :
                ServerResponse.badRequest().bodyValue("b can not be 0"));
    }

    public Mono<ServerResponse> divisionHandler(ServerRequest serverRequest){
        return process(serverRequest, (a, b) -> ServerResponse.ok().bodyValue(a+" / "+b+" = "+(a/b)));
    }

    private Mono<ServerResponse> process(ServerRequest request, BiFunction<Integer, Integer, Mono<ServerResponse>> opLogic){
        int a = getValue(request, "a");
        int b = getValue(request, "b");
        return opLogic.apply(a, b);
    }
    private int getValue(ServerRequest request, String key){
        return Integer.parseInt(request.pathVariable(key));
    }
}
