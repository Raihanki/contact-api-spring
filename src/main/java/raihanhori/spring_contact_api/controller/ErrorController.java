package raihanhori.spring_contact_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.ConstraintViolationException;
import raihanhori.spring_contact_api.model.ApiResponse;

@RestControllerAdvice
public class ErrorController {

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse<String>> constraintViolation(ConstraintViolationException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ApiResponse.<String>builder().errors(exception.getMessage()).build());
	}
	
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ApiResponse<String>> responseStatus(ResponseStatusException exception) {
		return ResponseEntity.status(exception.getStatusCode())
				.body(ApiResponse.<String>builder().errors(exception.getMessage()).build());
	}
}
