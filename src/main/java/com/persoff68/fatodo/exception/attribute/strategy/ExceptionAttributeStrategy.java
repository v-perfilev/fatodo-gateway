package com.persoff68.fatodo.exception.attribute.strategy;

import org.springframework.http.server.reactive.ServerHttpRequest;

public final class ExceptionAttributeStrategy extends AbstractAttributeStrategy {

    private final ServerHttpRequest request;

    public ExceptionAttributeStrategy(ServerHttpRequest request, Exception exception) {
        super(exception);
        this.request = request;
    }

    @Override
    public void addPath() {
        String path = request.getURI().getPath();
        if (path != null) {
            errorAttributes.put("path", path);
        }
    }

}
