package com.persoff68.fatodo.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.exception.AbstractException;
import com.persoff68.fatodo.exception.attribute.AttributeHandler;
import com.persoff68.fatodo.security.exception.ForbiddenException;
import com.persoff68.fatodo.security.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SecurityProblemSupport implements ServerAuthenticationEntryPoint, ServerAccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        Exception exception = e instanceof InternalAuthenticationServiceException
                && e.getCause() instanceof AbstractException
                ? (AbstractException) e.getCause()
                : new UnauthorizedException();

        return writeToResponse(request, response, exception);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        Exception exception = new ForbiddenException();

        return writeToResponse(request, response, exception);
    }

    private Mono<Void> writeToResponse(ServerHttpRequest request, ServerHttpResponse response, Exception exception) {
        AttributeHandler attributeHandler = AttributeHandler.from(request, exception);
        HttpStatus status = attributeHandler.getStatus();
        Map<String, Object> errorAttributes = attributeHandler.getErrorAttributes();

        Mono<DataBuffer> dataBuffer = Mono
                .fromCallable(() -> objectMapper.writeValueAsString(errorAttributes))
                .map(String::getBytes)
                .map(b -> response.bufferFactory().wrap(b));

        response.setStatusCode(status);
        return response.writeWith(dataBuffer);
    }

}
