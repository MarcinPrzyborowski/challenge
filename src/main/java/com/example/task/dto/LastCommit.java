package com.example.task.dto;

import lombok.Builder;

@Builder
public record LastCommit(String sha) { }
