package com.persoff68.fatodo.exception.attribute.strategy;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;

public final class ExceptionAttributeStrategy extends AbstractAttributeStrategy {

    private final ServerHttpRequest request;

    public ExceptionAttributeStrategy(ServerHttpRequest request, Throwable exception) {
        super(exception);
        this.request = request;
    }

    @Override
    public HttpStatus getStatus() {
        return exception instanceof AbstractException && ((AbstractException) exception).getStatus() != null
                ? ((AbstractException) exception).getStatus()
                : HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    public void addPath() {
        String path = request.getURI().getPath();
        if (path != null) {
            errorAttributes.put("path", path);
        }
    }

}
