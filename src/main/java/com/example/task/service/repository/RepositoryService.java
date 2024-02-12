package com.example.task.service.repository;

import com.example.task.dto.Repository;
import reactor.core.publisher.Flux;

public interface RepositoryService {
    Flux<Repository> getRepositories(final String username);
}
