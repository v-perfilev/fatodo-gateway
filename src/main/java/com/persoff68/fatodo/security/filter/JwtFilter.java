package com.persoff68.fatodo.security.filter;

import com.persoff68.fatodo.config.AppProperties;
import com.persoff68.fatodo.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

@Component
@RequiredArgsConstructor
public class JwtFilter implements WebFilter {

    private final AppProperties appProperties;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Nonnull
    public Mono<Void> filter(@Nonnull ServerWebExchange exchange, @Nonnull WebFilterChain chain) {
        String jwt = extractJwtFromExchange(exchange);
        Authentication authentication = extractAuthenticationFromJwt(jwt);

        return authentication != null
                ? chain.filter(exchange)
                .subscriberContext(ReactiveSecurityContextHolder.withAuthentication(authentication))
                : chain.filter(exchange);
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
