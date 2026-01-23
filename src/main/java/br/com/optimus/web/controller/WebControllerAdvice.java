package br.com.optimus.web.controller;

import br.com.optimus.web.exception.UnauthorizedException;
import br.com.optimus.web.exception.ValidationException;
import br.com.optimus.web.record.ErrorDto;
import br.com.optimus.web.record.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class WebControllerAdvice extends ResponseEntityExceptionHandler{

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<Response> handleValidationException(ValidationException ex) {
		ErrorDto error = new ErrorDto(ex.getMessage());
		return new ResponseEntity<>(new Response(List.of(error)), HttpStatus.BAD_REQUEST);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
	  List<ErrorDto> errors = this.createListErrors(ex.getBindingResult());
	  return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	private List<ErrorDto> createListErrors(BindingResult bindingResult){
		List<ErrorDto> erros = new ArrayList<>();
		bindingResult.getFieldErrors().forEach(fieldError->{
			String messageInterface = fieldError.getDefaultMessage();
			erros.add(new ErrorDto(messageInterface));
		});
		return erros;
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<Response> handleUnauthorizedException(UnauthorizedException ex) {
		ErrorDto error = new ErrorDto(ex.getMessage());
		return new ResponseEntity<>(new Response(List.of(error)), HttpStatus.UNAUTHORIZED);
	}
}