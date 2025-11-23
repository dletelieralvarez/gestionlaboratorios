package com.example.gestionlaboratorios.comun.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.validation.ConstraintViolationException;

import com.example.gestionlaboratorios.comun.model.ApiResult;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Cuando falla la validación del DTO (@Valid en el @RequestBody)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {

        List<String> errores = ex.getBindingResult()
                                 .getFieldErrors()
                                 .stream()
                                 .map(error -> error.getDefaultMessage())
                                 .collect(Collectors.toList());

        ApiResult<List<String>> respuesta = new ApiResult<>(
            "Error de validación de campos",
            errores,
            HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.badRequest().body(respuesta);
    }

    // Cuando falla la validación de la entidad al guardar (ConstraintViolationException)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResult<String>> handleConstraintViolation(ConstraintViolationException ex) {
        String mensaje = ex.getConstraintViolations().iterator().next().getMessage();

        ApiResult<String> respuesta = new ApiResult<>(
            mensaje,   // mensaje amigable desde el @Message de la anotación
            null,
            HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.badRequest().body(respuesta);
    }
}