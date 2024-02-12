package com.example.task.controller.repository;

import com.example.task.controller.error.ApiErrorResponse;
import com.example.task.controller.repository.response.RepositoryResponse;
import com.example.task.dto.Repository;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 8081)
class RepositoryControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getRepositoriesByUsernameShouldReturnRepositories() throws IOException {

        String responseBody = IOUtils.resourceToString("/data/repository-response.json", StandardCharsets.UTF_8);
        int STATUS_OK = 200;

        prepareStubFor("/users/octocat/repos", STATUS_OK, responseBody);

        String branches = IOUtils.resourceToString("/data/branches-response.json", StandardCharsets.UTF_8);
        prepareStubFor("/repos/octocat/Hello-World/branches", STATUS_OK, branches);

        String commits = IOUtils.resourceToString("/data/commits-response.json", StandardCharsets.UTF_8);
        prepareStubFor("/repos/octocat/Hello-World/commits/master", STATUS_OK, commits);

       webTestClient.get().uri("/users/{username}/repositories", "octocat")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("Hello-World")
                .jsonPath("$[0].branches[0].name").isEqualTo("master")
                .jsonPath("$[0].branches[0].lastCommitSha").isEqualTo("6dcb09b5b57875f334f61aebed695e2e4193db5e")
                .jsonPath("$[0].owner.login").isEqualTo("octocat");
    }

    @Test
    void shouldReturnNotFoundForInvalidUsername() {
        prepareStubFor("/repos/octocat/Hello-World/commits/master", 404, null);

        webTestClient.get().uri("/users/{username}/repositories", "asdasd")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectBodyList(ApiErrorResponse.class)
                .returnResult().getResponseBody();
    }

    @Test
    void shouldReturnNotAcceptableForUnsupportedMediaType() {
        webTestClient.get().uri("/users/{username}/repositories", "asdasd")
                .accept(MediaType.APPLICATION_XML)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_ACCEPTABLE)
                .expectBody(ApiErrorResponse.class)
                .returnResult().getResponseBody();
    }

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("git-hub.base-url", () -> "http://localhost:8081");
    }

    void prepareStubFor(String url, int status, String responseBody) {
        stubFor(get(url).willReturn(
                aResponse().withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)
        ));
    }

}