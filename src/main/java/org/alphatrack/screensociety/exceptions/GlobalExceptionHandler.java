package org.alphatrack.screensociety.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorObject> handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest path) {

        ErrorObject error = ErrorObject.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(e.getMessage())
                .path(path.getRequestURI())
                .timeStamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<ErrorObject> handleDuplicateEntityException(DuplicateEntityException e, HttpServletRequest path) {

        ErrorObject error = ErrorObject.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .error("Conflict")
                .message(e.getMessage())
                .path(path.getRequestURI())
                .timeStamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthorizationFailureException.class)
    public ResponseEntity<ErrorObject> handleAuthorizationFailureException(AuthorizationFailureException e, HttpServletRequest path){

        ErrorObject error = ErrorObject.builder()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .error("Unauthorized")
                .message(e.getMessage())
                .path(path.getRequestURI())
                .timeStamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorObject> handleSpringSecurityAccessDeniedException(AccessDeniedException e,HttpServletRequest path){

        ErrorObject error = ErrorObject.builder()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .error("Forbidden")
                .message(e.getMessage())
                .path(path.getRequestURI())
                .timeStamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error,HttpStatus.FORBIDDEN);
    }
}
