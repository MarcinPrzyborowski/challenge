package com.example.task.service.git;

import com.example.task.dto.Branch;
import com.example.task.dto.LastCommit;
import com.example.task.dto.Repository;
import com.example.task.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GithubServiceImpl implements GitService {

    private final WebClient webClient;

    public Flux<Repository> getRepositories(final String username) {
        String errorMessage = String.format("Repository for user %s not found", username);
        return executeGetRequest("/users/{username}/repos", Repository.class, errorMessage, username);
    }

    @Override
    public Flux<Branch> getBranches(String username, String repositoryName) {
        String errorMessage = String.format("Branches for username %s and repository login %s not found!", username, repositoryName);
        return executeGetRequest("/repos/{username}/{repositoryName}/branches", Branch.class, errorMessage, username, repositoryName);
    }

    public Flux<LastCommit> getLastCommit(final String username, final String repositoryName, String branchName) {
        String errorMessage = String.format("Commits for username %s, repository %s, and branch %s not found!", username, repositoryName, branchName);
        return executeGetRequest("/repos/{username}/{repositoryName}/commits/{branchName}", LastCommit.class, errorMessage, username, repositoryName, branchName);
    }

    private <T> Flux<T> executeGetRequest(String uriTemplate, Class<T> clazz, String errorMessage, Object... uriVariables) {
        return webClient.get()
                .uri(uriTemplate, uriVariables)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, response -> Mono.error(new NotFoundException(errorMessage)))
                .bodyToFlux(clazz);
    }
}
