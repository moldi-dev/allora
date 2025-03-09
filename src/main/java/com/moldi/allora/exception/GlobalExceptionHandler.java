package com.moldi.allora.exception;

import com.moldi.allora.service.implementation.AuthenticationService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final AuthenticationService authenticationService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> validationErrors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError) {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                validationErrors.put(fieldName, errorMessage);
            }

            else {
                String objectName = error.getObjectName();
                String errorMessage = error.getDefaultMessage();
                validationErrors.put(objectName, errorMessage);
            }
        });

        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .timestamp(LocalDateTime.now().toString())
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .errorStatus(HttpStatus.BAD_REQUEST)
                .errorMessage("Validation failed")
                .validationErrors(validationErrors)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(WebRequest request) {
        ErrorResponse response = ErrorResponse
                .builder()
                .timestamp(LocalDateTime.now().toString())
                .errorMessage("The body of the request is missing")
                .errorStatus(HttpStatus.BAD_REQUEST)
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .requestPath(request.getDescription(false))
                .build();

        return new ResponseEntity<>(response, response.getErrorStatus());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExistsException(ResourceAlreadyExistsException exception, WebRequest request) {
        ErrorResponse response = ErrorResponse
                .builder()
                .timestamp(LocalDateTime.now().toString())
                .errorMessage(exception.getMessage())
                .errorStatus(HttpStatus.CONFLICT)
                .errorCode(HttpStatus.CONFLICT.value())
                .requestPath(request.getDescription(false))
                .build();

        return new ResponseEntity<>(response, response.getErrorStatus());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {
        ErrorResponse response = ErrorResponse
                .builder()
                .timestamp(LocalDateTime.now().toString())
                .errorMessage(exception.getMessage())
                .errorStatus(HttpStatus.NOT_FOUND)
                .errorCode(HttpStatus.NOT_FOUND.value())
                .requestPath(request.getDescription(false))
                .build();

        return new ResponseEntity<>(response, response.getErrorStatus());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException exception, WebRequest request) {
        ErrorResponse response = ErrorResponse
                .builder()
                .timestamp(LocalDateTime.now().toString())
                .errorMessage(exception.getMessage())
                .errorStatus(HttpStatus.BAD_REQUEST)
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .requestPath(request.getDescription(false))
                .build();

        return new ResponseEntity<>(response, response.getErrorStatus());
    }

    @ExceptionHandler({AccountNotFoundException.class, MailAuthenticationException.class, BadCredentialsException.class, MalformedJwtException.class, SignatureException.class, ExpiredJwtException.class, InsufficientAuthenticationException.class, AccessDeniedException.class, DisabledException.class, LockedException.class})
    public ResponseEntity<ErrorResponse> handleSecurityExceptions(Exception exception, WebRequest request, HttpServletResponse servletResponse) {
        ErrorResponse response = ErrorResponse.builder().build();
        HttpStatus status = null;

        if (exception instanceof MailAuthenticationException) {
            response.setErrorMessage("An unexpected error occured while trying to send the email. Please try again later");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        else if (exception instanceof BadCredentialsException) {
            response.setErrorMessage("Invalid credentials");
            status = HttpStatus.NOT_FOUND;
        }

        else if (exception instanceof SignatureException) {
            authenticationService.signOut(servletResponse);

            response.setErrorMessage("The JWT signature is invalid");
            status = HttpStatus.UNAUTHORIZED;
        }

        else if (exception instanceof ExpiredJwtException) {
            authenticationService.signOut(servletResponse);

            response.setErrorMessage("The JWT has expired");
            status = HttpStatus.UNAUTHORIZED;
        }

        else if (exception instanceof MalformedJwtException) {
            authenticationService.signOut(servletResponse);

            response.setErrorMessage(exception.getMessage());
            status = HttpStatus.UNAUTHORIZED;
        }

        else if (exception instanceof AccountNotFoundException) {
            authenticationService.signOut(servletResponse);

            response.setErrorMessage(exception.getMessage());
            status = HttpStatus.UNAUTHORIZED;
        }

        else if (exception instanceof InsufficientAuthenticationException || exception instanceof AccessDeniedException) {
            response.setErrorMessage("You are not authorized to access this resource");
            status = HttpStatus.UNAUTHORIZED;
        }

        else if (exception instanceof DisabledException) {
            response.setErrorMessage("This account is not yet verified. You will be notified on your email when your registration is accepted or denied");
            status = HttpStatus.UNAUTHORIZED;
        }

        else if (exception instanceof LockedException) {
            response.setErrorMessage("This account has been suspended for not complying with the website's terms of use");
            status = HttpStatus.FORBIDDEN;
        }

        response.setTimestamp(LocalDateTime.now().toString());
        response.setErrorCode(status.value());
        response.setErrorStatus(status);
        response.setRequestPath(request.getDescription(false));

        return new ResponseEntity<>(response, response.getErrorStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception, WebRequest request) {
        ErrorResponse response = ErrorResponse
                .builder()
                .timestamp(LocalDateTime.now().toString())
                .errorMessage(exception.getMessage())
                .errorStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .requestPath(request.getDescription(false))
                .build();

        return new ResponseEntity<>(response, response.getErrorStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception, WebRequest request) {
        ErrorResponse response = ErrorResponse
                .builder()
                .timestamp(LocalDateTime.now().toString())
                .errorMessage(exception.getMessage())
                .errorStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .requestPath(request.getDescription(false))
                .build();

        return new ResponseEntity<>(response, response.getErrorStatus());
    }
}
