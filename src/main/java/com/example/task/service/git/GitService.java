package com.example.task.service.git;

import com.example.task.dto.Branch;
import com.example.task.dto.LastCommit;
import com.example.task.dto.Repository;
import reactor.core.publisher.Flux;

public interface GitService {

    Flux<Repository> getRepositories(final String username);

    Flux<Branch> getBranches(final String username, final String repositoryName);

    Flux<LastCommit> getLastCommit(final String username, final String repositoryName, String branchName);
}
