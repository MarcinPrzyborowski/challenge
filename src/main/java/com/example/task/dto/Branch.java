package com.example.task.dto;

import lombok.Builder;

@Builder
public record Branch(String name, String lastCommitSha) { }
