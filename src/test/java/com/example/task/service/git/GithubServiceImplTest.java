package com.example.task.service.git;

import com.example.task.dto.Branch;
import com.example.task.dto.LastCommit;
import com.example.task.dto.Owner;
import com.example.task.dto.Repository;
import com.example.task.exception.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

public class GithubServiceImplTest {

    private MockWebServer mockWebServer;
    private GithubServiceImpl githubService;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        githubService = new GithubServiceImpl(WebClient.builder().baseUrl(mockWebServer.url("/").toString()).build());
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getRepositoriesShouldReturnFluxOfRepositories() throws JsonProcessingException {
        mockWebServer.enqueue(new MockResponse()
                .setBody(new ObjectMapper().writeValueAsString(List.of(new Repository("mock-repo", List.of(), new Owner("mock-owner"), false))))
                .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        StepVerifier.create(githubService.getRepositories("mock-user"))
                .expectNextMatches(repo -> "mock-repo".equals(repo.name()) && "mock-owner".equals(repo.owner().login()))
                .verifyComplete();
    }

    @Test
    void getRepositoriesShouldHandleNotFound() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        StepVerifier.create(githubService.getRepositories("nonexistent-user"))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException && throwable.getMessage().contains("Repository for user nonexistent-user not found"))
                .verify();
    }

    @Test
    void getBranchesShouldReturnFluxOfBranches() throws JsonProcessingException {
        mockWebServer.enqueue(new MockResponse()
                .setBody(new ObjectMapper().writeValueAsString(List.of(new Branch("main", "sha12345"))))
                .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        StepVerifier.create(githubService.getBranches("mock-user", "mock-repo"))
                .expectNextMatches(b -> "main".equals(b.name()) && "sha12345".equals(b.lastCommitSha()))
                .verifyComplete();
    }

    @Test
    void getBranchesShouldHandleNotFound() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        StepVerifier.create(githubService.getBranches("nonexistent-user", "nonexistent-repo"))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException && throwable.getMessage().contains("Branches for username nonexistent-user and repository login nonexistent-repo not found!"))
                .verify();
    }

    @Test
    void getLastCommitShouldReturnFluxOfLastCommits() throws JsonProcessingException {
        mockWebServer.enqueue(new MockResponse()
                .setBody(new ObjectMapper().writeValueAsString(List.of(new LastCommit("sha12345"))))
                .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        StepVerifier.create(githubService.getLastCommit("mock-user", "mock-repo", "main"))
                .expectNextMatches(commit -> "sha12345".equals(commit.sha()))
                .verifyComplete();
    }

    @Test
    void getLastCommitShouldHandleNotFound() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        StepVerifier.create(githubService.getLastCommit("nonexistent-user", "nonexistent-repo", "nonexistent-branch"))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException && throwable.getMessage().contains("Commits for username nonexistent-user, repository nonexistent-repo, and branch nonexistent-branch not found!"))
                .verify();
    }
}