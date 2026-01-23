package br.com.optimus.web.record.response;

public record StatusCardResponseDto(
        long total,
        long active,
        long inactive
) {}