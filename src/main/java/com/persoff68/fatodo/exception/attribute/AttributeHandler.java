package com.persoff68.fatodo.exception.attribute;

import com.persoff68.fatodo.exception.attribute.strategy.AttributeStrategy;
import com.persoff68.fatodo.exception.attribute.strategy.ExceptionAttributeStrategy;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

public class AttributeHandler {

    private final AttributeStrategy attributeStrategy;

    private AttributeHandler(ServerHttpRequest request, Exception exception) {
        this.attributeStrategy = new ExceptionAttributeStrategy(request, exception);
    }

    public static AttributeHandler from(ServerHttpRequest request, Exception exception) {
        return new AttributeHandler(request, exception);
    }

    public Map<String, Object> getErrorAttributes() {
        attributeStrategy.addTimestamp();
        attributeStrategy.addStatus();
        attributeStrategy.addErrorDetails();
        attributeStrategy.addFeedbackCode();
        attributeStrategy.addPath();
        return attributeStrategy.getErrorAttributes();
    }

    public HttpStatus getStatus() {
        return attributeStrategy.getStatus();
    }

    public Mono<ServerResponse> buildResponse() {
        HttpStatus status = getStatus();
        return ServerResponse
                .status(status)
                .body(getErrorAttributes(), String.class);
    }

}
