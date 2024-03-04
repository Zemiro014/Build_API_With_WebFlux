package com.jeronimo.webfluxdemo.controller;

import com.jeronimo.webfluxdemo.dto.MultiplyRequestDto;
import com.jeronimo.webfluxdemo.dto.Response;
import com.jeronimo.webfluxdemo.exception.InputFailedValidationException;
import com.jeronimo.webfluxdemo.service.ReactiveMathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("reactive-math")
public class ReactiveMathValidationController {

    @Autowired
    private ReactiveMathService mathService;

    @GetMapping("square/{input}/throw")
    public Mono<Response> findSquare(@PathVariable int input){
        if(input < 10 || input > 20)
            throw new InputFailedValidationException(input);
        return this.mathService.findSquare(input);
    }

    @GetMapping("square/{input}/mono-error")
    public Mono<Response> monoError(@PathVariable int input){
        return Mono.just(input)
            .handle((integer, sink) -> {
                if(integer >= 10 && integer <=20){
                    sink.next(integer);
                } else
                    sink.error(new InputFailedValidationException(integer));
            })
            .cast(Integer.class)
            .flatMap(i -> this.mathService.findSquare(i));
    }
}
