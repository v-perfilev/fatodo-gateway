package com.persoff68.fatodo.security.filter;

import com.persoff68.fatodo.config.AppProperties;
import com.persoff68.fatodo.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationConverter implements ServerAuthenticationConverter {
    private final AppProperties appProperties;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange)
                .flatMap(ex -> Mono.justOrEmpty(extractJwtFromExchange(ex)))
                .flatMap(jwt -> Mono.justOrEmpty(extractAuthenticationFromJwt(jwt)));
    }

    private String extractJwtFromExchange(ServerWebExchange exchange) {
        String authHeader = appProperties.getAuth().getAuthorizationHeader();
        String authPrefix = appProperties.getAuth().getAuthorizationPrefix();

        ServerHttpRequest request = exchange.getRequest();
        String bearerToken = request.getHeaders().getFirst(authHeader);

        boolean hasToken = StringUtils.hasText(bearerToken) && bearerToken.startsWith(authPrefix);
        return hasToken ? bearerToken.substring(authPrefix.length()) : null;
    }

    private Authentication extractAuthenticationFromJwt(String jwt) {
        boolean hasJwt = StringUtils.hasText(jwt) && jwtTokenProvider.validateJwt(jwt);
        return hasJwt
                ? jwtTokenProvider.getAuthenticationFromJwt(jwt)
                : null;
    }

}
