package com.example.task.service.repository;

import com.example.task.dto.Branch;
import com.example.task.dto.LastCommit;
import com.example.task.dto.Repository;
import com.example.task.service.git.GitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RepositoryServiceImpl implements RepositoryService {

    private final GitService gitService;

    public Flux<Repository> getRepositories(final String username) {
        return gitService.getRepositories(username)
                .filter(this::isNotFork)
                .flatMap(repository -> enrichRepositoryWithBranches(username, repository));
    }

    private boolean isNotFork(Repository repository) {
        return !repository.fork();
    }

    private Flux<Repository> enrichRepositoryWithBranches(String username, Repository repository) {
        return gitService.getBranches(username, repository.name())
                .flatMap(branch -> enrichBranchWithLastCommit(username, repository, branch))
                .collectList()
                .flatMapMany(branches -> Flux.just(buildRepository(repository, branches)));
    }

    private Flux<Branch> enrichBranchWithLastCommit(String username, Repository repository, Branch branch) {
        return gitService.getLastCommit(username, repository.name(), branch.name())
                .map(lastCommit -> buildBranchWithLastCommit(branch, lastCommit))
                .defaultIfEmpty(buildBranchWithoutLastCommit(branch));
    }

    private Branch buildBranchWithLastCommit(Branch branch, LastCommit lastCommit) {
        return Branch.builder()
                .name(branch.name())
                .lastCommitSha(lastCommit.sha())
                .build();
    }

    private Branch buildBranchWithoutLastCommit(Branch branch) {
        return Branch.builder().name(branch.name()).build();
    }

    private Repository buildRepository(Repository repository, List<Branch> branches) {
        return Repository.builder()
                .name(repository.name())
                .owner(repository.owner())
                .branches(branches)
                .build();
    }
}
