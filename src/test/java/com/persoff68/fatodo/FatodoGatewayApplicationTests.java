package com.persoff68.fatodo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(classes = FatodoGatewayApplication.class)
@AutoConfigureWebTestClient
class FatodoGatewayApplicationTests {

    @Autowired
    WebTestClient webTestClient;

    @Test
    @WithMockUser
    void contextLoads() {
        FatodoGatewayApplication.main(new String[]{});
        webTestClient
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @WithMockUser
    void testWrongPath() {
        webTestClient
                .get()
                .uri("/wrong-path")
                .exchange()
                .expectStatus().isNotFound();
    }

}
