package com.persoff68.fatodo.exception.attribute.strategy;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

public final class GlobalAttributeStrategy extends AbstractAttributeStrategy {

    private final ServerRequest request;

    public GlobalAttributeStrategy(ServerRequest request, Throwable exception) {
        super(exception);
        this.request = request;
    }

    @Override
    public HttpStatus getStatus() {
        HttpStatus status;
        if (exception instanceof AbstractException abstractException) {
            status = abstractException.getStatus();
        } else if (exception instanceof ResponseStatusException responseStatusException) {
            status = responseStatusException.getStatus();
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return status;
    }

    @Override
    public void addPath() {
        String path = request.path();
        if (!path.isEmpty()) {
            errorAttributes.put("path", path);
        }
    }

}
