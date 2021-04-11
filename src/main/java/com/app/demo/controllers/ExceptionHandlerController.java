package com.app.demo.controllers;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.app.demo.dtos.ErrorMessage;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.dao.DataIntegrityViolationException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

@RestControllerAdvice
public class ExceptionHandlerController {
	
	 @ExceptionHandler(Exception.class)
	    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	    public ErrorMessage handleAllException(Exception ex, WebRequest request) {
	        return new ErrorMessage(10000, ex.getLocalizedMessage());
	    }
	
	 @ExceptionHandler(IndexOutOfBoundsException.class)
	    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
	    public ErrorMessage TodoException(Exception ex,  WebRequest request) {
	        return new ErrorMessage(10100, "Đối tượng không tồn tại");
	    }
	
}
