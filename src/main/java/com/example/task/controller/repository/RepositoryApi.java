package com.example.task.controller.repository;

import com.example.task.controller.repository.response.RepositoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("users")
interface RepositoryApi {

    @Operation(summary = "Get user repositories")
    @GetMapping(value = "/{username}/repositories", produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<ResponseEntity<Flux<RepositoryResponse>>> getRepositoriesByUsername(@PathVariable String username);
}
