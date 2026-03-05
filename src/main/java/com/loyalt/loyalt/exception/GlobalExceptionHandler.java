package com.loyalt.loyalt.exception;

import com.loyalt.loyalt.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(LoyaltyClassAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleLoyaltyClassExistsException(LoyaltyClassAlreadyExistsException ex, HttpServletRequest request){
        ErrorResponse error = new ErrorResponse(
                Instant.now().toString(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(GoogleWalletException.class)
    public ResponseEntity<ErrorResponse> handleGoogleWalletException(GoogleWalletException ex, HttpServletRequest request){
        ErrorResponse error = new ErrorResponse(
                Instant.now().toString(),
                HttpStatus.BAD_GATEWAY.value(),
                HttpStatus.BAD_GATEWAY.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);


    }
}
