package com.persoff68.fatodo.exception.attribute.strategy;

import com.persoff68.fatodo.exception.AbstractException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
public final class GlobalAttributeStrategy extends AbstractAttributeStrategy {

    private final ServerRequest request;
    private final Throwable exception;

    @Override
    public HttpStatus getStatus() {
        HttpStatus status = null;
        if (exception instanceof AbstractException) {
            status = ((AbstractException) exception).getStatus();
        } else if (exception instanceof ResponseStatusException) {
            status = ((ResponseStatusException) exception).getStatus();
        }
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return status;
    }

    @Override
    public String getFeedbackCode() {
        return exception instanceof AbstractException && ((AbstractException) exception).getFeedBackCode() != null
                ? ((AbstractException) exception).getFeedBackCode()
                : null;
    }

    @Override
    public void addErrorDetails() {
        String message = exception.getMessage();
        if (message != null) {
            errorAttributes.put("message", message);
        }
    }

    @Override
    public void addPath() {
        String path = request.path();
        if (!path.isEmpty()) {
            errorAttributes.put("path", path);
        }
    }

}
