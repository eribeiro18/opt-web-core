package br.com.optimus.web.record;

import java.util.List;

public record Response (
		List<?> response,
		Integer totalPages,
		Long totalRecords,
		List<ErrorDto> errors
) {
	public Response(List<?> response){
		this(response, null, null, null);
	}

	public Response(List<?> response, Integer totalPages, Long totalRecords){
		this(response, totalPages, totalRecords, null);
	}

	public Response(){
		this(null, null, null, null);
	}
}
