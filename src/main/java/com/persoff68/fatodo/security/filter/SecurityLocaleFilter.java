package com.persoff68.fatodo.security.filter;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Locale;

@Component
public class SecurityLocaleFilter implements WebFilter {

    @Override
    @Nonnull
    public Mono<Void> filter(@Nonnull ServerWebExchange exchange, @Nonnull WebFilterChain chain) {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        return chain.filter(exchange);
    }

}
