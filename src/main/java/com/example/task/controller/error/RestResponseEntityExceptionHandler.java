package com.example.task.controller.error;

import com.example.task.controller.error.ApiErrorResponse;
import com.example.task.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.NotAcceptableStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import reactor.core.publisher.Mono;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    protected Mono<ResponseEntity<Object>> handleNotFoundException(NotFoundException ex) {
        return buildViolation(HttpStatus.NOT_FOUND, ex);
    }

    @Override
    protected Mono<ResponseEntity<Object>> handleNotAcceptableStatusException(NotAcceptableStatusException ex, HttpHeaders headers, HttpStatusCode status, ServerWebExchange exchange) {
        return buildViolation(HttpStatus.NOT_ACCEPTABLE, ex);
    }

    @Override
    protected Mono<ResponseEntity<Object>> handleUnsupportedMediaTypeStatusException(UnsupportedMediaTypeStatusException ex, HttpHeaders headers, HttpStatusCode status, ServerWebExchange exchange) {
        return buildViolation(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex);
    }

    private Mono<ResponseEntity<Object>> buildViolation(HttpStatus status, Exception ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(status.toString(), ex.getMessage());
        return Mono.just(new ResponseEntity<>(apiErrorResponse, status));
    }
}
