package com.vimal.app.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
	
	private static final long serialVersionUID = 3334908591754672801L;
	
	private final HttpStatus status;
	
	private final String error;

	public AppException(String error, String message, HttpStatus status) {
        super(message);
        this.error = error;
        this.status = status;
    }
    
}
