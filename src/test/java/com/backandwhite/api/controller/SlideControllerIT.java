package com.backandwhite.api.controller;

import com.backandwhite.BaseIntegrationTest;
import com.backandwhite.core.test.JwtTestUtil;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class SlideControllerIT extends BaseIntegrationTest {

    @Autowired
    private JwtTestUtil jwtTestUtil;

    @Test
    void getActiveSlides_publicEndpoint_returns200() {
        webTestClient.get().uri("/api/v1/slides/active").header("X-nx036-auth", "internal-test-token").exchange()
                .expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody().jsonPath("$")
                .isArray();
    }

    @Test
    void findAllSlides_withAdminToken_returns200() {
        String adminToken = jwtTestUtil.getToken("admin-user", List.of("ADMIN"));

        webTestClient.get().uri("/api/v1/slides").header("Authorization", adminToken)
                .header("X-nx036-auth", "internal-test-token").exchange().expectStatus().isOk().expectBody()
                .jsonPath("$").isArray();
    }
}
