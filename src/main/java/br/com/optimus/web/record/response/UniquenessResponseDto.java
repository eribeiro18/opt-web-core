package br.com.optimus.web.record.response;

public record UniquenessResponseDto(
        boolean available,
        String message
) {}