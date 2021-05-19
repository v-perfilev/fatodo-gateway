package com.persoff68.fatodo.exception.handler;

import com.persoff68.fatodo.exception.AbstractException;
import com.persoff68.fatodo.exception.attribute.AttributeHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
public class ExceptionHandlerSet {

    @ExceptionHandler(AbstractException.class)
    public Mono<ServerResponse> handleAbstractException(ServerHttpRequest request, AbstractException e) {
        return AttributeHandler.from(request, e).buildResponse();
    }

    @ExceptionHandler(RuntimeException.class)
    public Mono<ServerResponse> handleRuntimeException(ServerHttpRequest request, RuntimeException e) {
        return e.getCause() instanceof Exception
                ? AttributeHandler.from(request, (Exception) e.getCause()).buildResponse()
                : AttributeHandler.from(request, e).buildResponse();
    }

    @ExceptionHandler(Exception.class)
    public Mono<ServerResponse> handleException(ServerHttpRequest request, Exception e) {
        return AttributeHandler.from(request, e).buildResponse();
    }

}
