package com.kan.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InvalidDataException.class)
	public ResponseEntity<Object> handleException(InvalidDataException e){
		
		 Map<String, Object> body = new LinkedHashMap<>();
	     body.put("timestamp", LocalDateTime.now());
	     body.put("error", "Invalid Data");
	     body.put("message", e.getMessage());
	        
		return ResponseEntity.badRequest().body(body);
	}
}
