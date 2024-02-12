package com.example.task.controller.repository;

import com.example.task.controller.repository.mapper.RepositoryMapper;
import com.example.task.controller.repository.response.RepositoryResponse;
import com.example.task.service.repository.RepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class RepositoryController implements RepositoryApi {

    private final RepositoryService repositoryService;
    private final RepositoryMapper repositoryMapper;

    public Mono<ResponseEntity<Flux<RepositoryResponse>>> getRepositoriesByUsername(@PathVariable String username) {
        return Mono.just(ResponseEntity.ok(repositoryService.getRepositories(username).map(repositoryMapper::repositoryToRepositoryResponse)));
    }

}
