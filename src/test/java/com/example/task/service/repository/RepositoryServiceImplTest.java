package com.example.task.service.repository;

import com.example.task.dto.Branch;
import com.example.task.dto.LastCommit;
import com.example.task.dto.Repository;
import com.example.task.service.git.GitService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class RepositoryServiceImplTest {

    @Mock
    private GitService gitService;

    @InjectMocks
    private RepositoryServiceImpl repositoryService;

    @Test
    void getRepositoriesShouldFilterForksAndEnrich() {
        Repository repo1 = Repository.builder().name("repo1").fork(false).build();
        Repository repo2 = Repository.builder().name("repo2").fork(true).build();
        Branch branch1 = Branch.builder().name("main").build();
        LastCommit commit1 = new LastCommit("sha12345");

        Mockito.when(gitService.getRepositories("user"))
                .thenReturn(Flux.just(repo1, repo2));
        Mockito.when(gitService.getBranches("user", "repo1"))
                .thenReturn(Flux.just(branch1));
        Mockito.when(gitService.getLastCommit("user", "repo1", "main"))
                .thenReturn(Flux.just(commit1));

        StepVerifier.create(repositoryService.getRepositories("user"))
                .expectNextMatches(repository ->
                        repository.name().equals("repo1") &&
                                repository.branches().get(0).name().equals("main") &&
                                repository.branches().get(0).lastCommitSha().equals("sha12345"))
                .verifyComplete();
    }

    @Test
    void getRepositoriesShouldHandleEmptyBranches() {
        Repository repo = Repository.builder().name("repo").fork(false).build();

        Mockito.when(gitService.getRepositories("user"))
                .thenReturn(Flux.just(repo));
        Mockito.when(gitService.getBranches("user", "repo"))
                .thenReturn(Flux.empty());

        StepVerifier.create(repositoryService.getRepositories("user"))
                .expectNextMatches(repository ->
                        repository.name().equals("repo") &&
                                repository.branches().isEmpty())
                .verifyComplete();
    }
}