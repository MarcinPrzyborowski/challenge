package com.example.task.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record Repository(String name, List<Branch> branches, Owner owner, boolean fork) {
}
