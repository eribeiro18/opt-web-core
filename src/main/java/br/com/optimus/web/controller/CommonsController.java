package br.com.optimus.web.controller;

import br.com.optimus.web.exception.UnauthorizedException;
import br.com.optimus.web.record.Response;
import br.com.optimus.web.record.UserJwtDto;
import jakarta.validation.ValidationException;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class CommonsController {

	@SneakyThrows
	public ResponseEntity<Response> buildResponse(HttpStatus status, Optional<?> response) {
		final Object o = response.orElseThrow(ClassNotFoundException::new);
        final List<?> respList = o instanceof Collection ? (List<?>) o : Collections.singletonList(o);
        return new ResponseEntity<>(new Response(respList), status);
	}

	@SneakyThrows
	public ResponseEntity<Response> buildResponse(HttpStatus status) {
		return new ResponseEntity<>(new Response(), status);
	}
	
	@SneakyThrows
	public ResponseEntity<Response> buildResponse(HttpStatus status, Optional<?> response, Integer totalPages,
												  Long totalRecords) {
		final Object o = response.orElseThrow(ClassNotFoundException::new);
        final List<?> respList = o instanceof Collection ? (List<?>) o : Collections.singletonList(o);
        
        return new ResponseEntity<>(new Response(respList, totalPages, totalRecords), status);
	}
	
	public UserJwtDto validateUser(String auth) throws UnauthorizedException {
		try {
			UserJwtDto userJwt = JsonUserProvider.getContextDto(auth);
			return userJwt;
		} catch (Exception e) {
			throw new ValidationException("Falha na validação do token! Token invalido ou mal formatado!", e);
		}
	}
}
