package com.persoff68.fatodo.exception.handler;

import com.persoff68.fatodo.exception.ClientException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(ExceptionController.ENDPOINT)
public class ExceptionController {
    static final String ENDPOINT = "/error";

    @GetMapping
    public Mono<Void> clientException() {
        return Mono.error(new ClientException());
    }

}
