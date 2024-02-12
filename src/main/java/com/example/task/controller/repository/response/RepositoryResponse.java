package com.example.task.controller.repository.response;

import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
public class RepositoryResponse {
    String name;
    List<Branch> branches;
    Owner owner;
    boolean fork;

    public record Owner(String login) {
    }

    public record Branch(String name, String lastCommitSha) {
    }

    public record LastCommit(String sha) {
    }
}


