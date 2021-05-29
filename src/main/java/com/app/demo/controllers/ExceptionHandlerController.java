package com.app.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.app.demo.dtos.ErrorMessage;

import org.springframework.security.authentication.BadCredentialsException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

@RestControllerAdvice
public class ExceptionHandlerController {
	/*@ExceptionHandler(Exception.class)
	 @ResponseStatus(value = HttpStatus.FORBIDDEN)
	    public ErrorMessage handleAllException(Exception ex, WebRequest request) {
	        return new ErrorMessage(403, ex.getMessage());
	    }
	    */
	
}
