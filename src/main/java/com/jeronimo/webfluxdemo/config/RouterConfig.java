package com.jeronimo.webfluxdemo.config;

import com.jeronimo.webfluxdemo.dto.InputFailedValidationResponse;
import com.jeronimo.webfluxdemo.exception.InputFailedValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@Configuration
public class RouterConfig {

    @Autowired
    private RequestHandler requestHandler;

    @Bean
    public RouterFunction<ServerResponse> highLevelRouter(){
        return RouterFunctions.route()
                .path("router", this::serverResponseRouterFunction)
                .build();
    }

    private RouterFunction<ServerResponse> serverResponseRouterFunction(){
        return RouterFunctions.route()
                .GET("square/{input}", RequestPredicates.path("*/1?"), requestHandler::squareHandler)
                .GET("square/{input}", request -> ServerResponse.badRequest().bodyValue("only 10-19 allow"))
                .GET("table/{input}", requestHandler::tableHandler)
                .GET("table/{input}/stream", requestHandler::tableStreamHandler)
                .POST("multiply", requestHandler::multiplyHandler)
                .GET("square/{input}/validation", requestHandler::squareHandlerValidation)
                .onError(InputFailedValidationException.class, exceptionHandler())
                .build();
    }

    private BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> exceptionHandler(){
        System.out.println("AQUIIIIIIIIIIII");
        return (err, req) -> {
            InputFailedValidationException exception = (InputFailedValidationException) err;
            InputFailedValidationResponse validationResponse = new InputFailedValidationResponse();
            validationResponse.setInput(exception.getInput());
            validationResponse.setMessage(exception.getMessage());
            validationResponse.setErrorCode(exception.getErrorCode());
            return ServerResponse.badRequest().bodyValue(validationResponse);
        };
    }
}
