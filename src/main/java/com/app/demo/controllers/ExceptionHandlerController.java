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
	 @ExceptionHandler(RuntimeException.class)
	    @ResponseStatus(value = HttpStatus.FORBIDDEN)
	    public ErrorMessage runTime(RuntimeException ex) {
		
	        return new ErrorMessage(403, ex.getMessage());
	    }
	 @ExceptionHandler(java.lang.ArrayIndexOutOfBoundsException.class)
	    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
	    public ErrorMessage indexOutOfBoundException(Exception ex,  WebRequest request) {
	        return new ErrorMessage(400, "Đối tượng đã tồn tại");
	    }
	 @ExceptionHandler(NullPointerException.class)   
	 @ResponseStatus(value = HttpStatus.NOT_FOUND)
	    public ErrorMessage catchNullPointerException(Exception ex,  WebRequest request) {
	        return new ErrorMessage(404, "Đối tượng không tồn tại");
	    }
	   
	 @ExceptionHandler(SQLIntegrityConstraintViolationException.class)   
	 @ResponseStatus(value = HttpStatus.CONFLICT)
	    public ErrorMessage catchSQLException(SQLIntegrityConstraintViolationException ex) {
		 String cmt = ex.getMessage();
		 return 
	        	 new ErrorMessage(409, "Error at "+cmt/*.split("'")[1]*/);
	    }
	 
	 @ExceptionHandler(IllegalStateException.class)   
	 @ResponseStatus(value = HttpStatus.CONFLICT)
	    public ErrorMessage catchIllegalStateException(IllegalStateException ex) {
		
		 return  new ErrorMessage(409, "Đối tượng đã tồn tại ");
	    }
	 
	 
	 @ExceptionHandler(HttpMessageNotReadableException.class)   
	 @ResponseStatus(value = HttpStatus.BAD_REQUEST)
	    public ErrorMessage catchRequest(Exception ex,  WebRequest request) {
	        return new ErrorMessage(409, "Giá trị thuộc tính sai định dạng");
	    }
	
	  @ExceptionHandler(MethodArgumentNotValidException.class)   
	 @ResponseStatus(value = HttpStatus.BAD_REQUEST)
	    public ErrorMessage catchValidate(Exception ex,  WebRequest request) {
	        return new ErrorMessage(400, "Giá trị thuộc tính sai định dạng");
	    }
	   /*@ExceptionHandler(ParseException.class)   
	 @ResponseStatus(value = HttpStatus.BAD_REQUEST)
	    public ErrorMessage catchParse(Exception ex,  WebRequest request) {
	        return new ErrorMessage(400, "Giá trị thuộc tính sai định dạng");
	    }*/
}
