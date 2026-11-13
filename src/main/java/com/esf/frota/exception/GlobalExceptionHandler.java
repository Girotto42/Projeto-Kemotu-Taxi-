package com.esf.frota.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ErroResposta(LocalDateTime timestamp, int status, Object detalhes) {}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errosCampos = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errosCampos.put(fieldName, errorMessage);
        });

        ErroResposta resposta = new ErroResposta(
            LocalDateTime.now(), 
            HttpStatus.BAD_REQUEST.value(), 
            errosCampos
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> handleGlobalException(Exception ex) {
        ErroResposta resposta = new ErroResposta(
            LocalDateTime.now(), 
            HttpStatus.INTERNAL_SERVER_ERROR.value(), 
            "Ocorreu um erro interno no servidor. Tente novamente mais tarde."
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resposta);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroResposta> handleStatusException(ResponseStatusException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        ErroResposta resposta = new ErroResposta(LocalDateTime.now(), status.value(),
                ex.getReason() == null ? status.getReasonPhrase() : ex.getReason());
        return ResponseEntity.status(status).body(resposta);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErroResposta> handleAuthenticationException(AuthenticationException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErroResposta resposta = new ErroResposta(LocalDateTime.now(), status.value(),
                "E-mail ou senha inválidos.");
        return ResponseEntity.status(status).body(resposta);
    }
}
