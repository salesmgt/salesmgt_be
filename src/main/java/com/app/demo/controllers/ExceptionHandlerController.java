package com.app.demo.controllers;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.security.authentication.BadCredentialsException;
import com.app.demo.exceptions.ErrorMessage;
import org.springframework.dao.DataIntegrityViolationException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

@RestControllerAdvice
public class ExceptionHandlerController {
/**
	/**
	 * Tất cả các Exception không được khai báo sẽ được xử lí tại đây
	 */
	/*
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleAllException(Exception ex, WebRequest request) {
        // quá trình kiểm soat lỗi diễn ra ở đây
        return new ErrorMessage(10000, ex.toString());
    }
	*/
	 /**
     * IndexOutOfBoundsException sẽ được xử lý riêng tại đây
     */
	/*
    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage undefinedObject(Exception ex, WebRequest request) {
        return new ErrorMessage(10100, "Không tìm thấy object");
    }
    
    @ExceptionHandler(ClassNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage notFoundClass(Exception ex, WebRequest request) {
        return new ErrorMessage(10200, "Kiểm tra phương thức GET/POST/PUT/DELETE");
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage conflict(Exception ex, WebRequest request) {
        return new ErrorMessage(10200, "Trùng dữ liệu trong hệ thống (username, phone, email)");
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage conflict2(Exception ex, WebRequest request) {
        return new ErrorMessage(10200, "Trùng dữ liệu trong hệ thống (username, phone, email)");
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage notFound(Exception ex, WebRequest request) {
        return new ErrorMessage(10100, "Lỗi format phone hoặc email");    
    }   
    
    @ExceptionHandler(BadCredentialsException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMessage unauthorizedException(Exception ex, WebRequest request) {
        return new ErrorMessage(2000, ex.getLocalizedMessage());
    }
    @ExceptionHandler(MalformedJwtException.class)
   	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
       public ErrorMessage unauthorized2Exception(Exception ex, WebRequest request) {
           return new ErrorMessage(2100, ex.getLocalizedMessage());
       }
        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
   	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
       public ErrorMessage wrongMethod(Exception ex, WebRequest request) {
           return new ErrorMessage(2100, "Request method not supported");
       }
       
*/
	
}
