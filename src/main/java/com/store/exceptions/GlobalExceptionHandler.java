package com.store.exceptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.store.dto.ErrorDetails;

/**
 * Esta clase maneja la captura de todas las excepciones de la aplicación Esto
 * permite que no salgan la larga fila de errores
 * 
 * @author leand
 *
 */
@ControllerAdvice // Le dice a la clase que le va a permitir manejar excepciones handler
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class) // Se encarga de las excepciones que se hayan detallado
	public ResponseEntity<ErrorDetails> handlingResourceNotFoundExceptions(ResourceNotFoundException exception,
			WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(FoodStoreAppException.class)
	public ResponseEntity<ErrorDetails> handlingFoodStoreAppException(FoodStoreAppException exception,
			WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> handlingGlobalException(Exception exception, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Esto permite mandar un mesaje cuando los datos que se mandan a métodos
	// validados no son correctos
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((e) -> {
			String fieldname = ((FieldError) e).getField();
			String message = e.getDefaultMessage();
			errors.put(fieldname, message);
		});
		;// Recupera todos los errores
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

}
