package com.persoff68.fatodo.security;

import com.persoff68.fatodo.FatodoGatewayApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;

@SpringBootTest(classes = FatodoGatewayApplication.class)
@AutoConfigureWebTestClient
class JwtFilterIT {

    @Value("${test.jwt.user}")
    String userJwt;
    @Value("${test.jwt.invalid-expired}")
    String invalidExpiredJwt;
    @Value("${test.jwt.invalid-format}")
    String invalidFormatJwt;
    @Value("${test.jwt.invalid-wrong-token}")
    String invalidWrongTokenJwt;
    @Value("${test.jwt.invalid-wrong-uuid}")
    String invalidWrongUuidJwt;

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testSuccessfulAuthorization() {
        webTestClient
                .get()
                .uri("/")
                .headers(setHeaders("Bearer " + userJwt))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testWrongPrefix() {
        webTestClient
                .get()
                .uri("/")
                .headers(setHeaders("WrongBearer " + userJwt))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testNoHeader() {
        webTestClient
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testInvalidExpiredJwt() {
        webTestClient
                .get()
                .uri("/")
                .headers(setHeaders("Bearer " + invalidExpiredJwt))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testInvalidEmptyJwt() {
        webTestClient
                .get()
                .uri("/")
                .headers(setHeaders("Bearer " + invalidFormatJwt))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testInvalidWrongTokenJwt() {
        webTestClient
                .get()
                .uri("/")
                .headers(setHeaders("Bearer " + invalidWrongTokenJwt))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testInvalidWrongUuidJwt() {
        webTestClient
                .get()
                .uri("/")
                .headers(setHeaders("Bearer " + invalidWrongUuidJwt))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    private Consumer<HttpHeaders> setHeaders(String authorizationHeader) {
        return headers -> headers.set("Authorization", authorizationHeader);
    }

}
