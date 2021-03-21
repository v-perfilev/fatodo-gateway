package com.persoff68.fatodo.exception.attribute;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable throwable = this.getError(request);
        return AttributeHandler.from(request, throwable).getErrorAttributes();
    }

    public HttpStatus getStatus(ServerRequest request) {
        Throwable throwable = this.getError(request);
        return AttributeHandler.from(request, throwable).getStatus();
    }

}
