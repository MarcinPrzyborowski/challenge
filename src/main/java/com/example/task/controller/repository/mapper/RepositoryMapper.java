package com.example.task.controller.repository.mapper;

import com.example.task.controller.repository.response.RepositoryResponse;
import com.example.task.dto.Repository;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RepositoryMapper {

    RepositoryResponse repositoryToRepositoryResponse(Repository repository);
}
