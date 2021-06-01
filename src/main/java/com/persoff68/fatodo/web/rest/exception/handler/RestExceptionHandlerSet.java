package com.persoff68.fatodo.web.rest.exception.handler;

import com.persoff68.fatodo.exception.attribute.AttributeHandler;
import com.persoff68.fatodo.web.rest.exception.InvalidFormException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class RestExceptionHandlerSet {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ServerResponse> handleMethodArgumentNotValidException(ServerHttpRequest request) {
        return handleInvalidFormException(request);
    }

    @ExceptionHandler(BindException.class)
    public Mono<ServerResponse> handleBindException(ServerHttpRequest request) {
        return handleInvalidFormException(request);
    }

    private Mono<ServerResponse> handleInvalidFormException(ServerHttpRequest request) {
        Exception e = new InvalidFormException();
        return AttributeHandler.from(request, e).buildResponse();
    }

}
