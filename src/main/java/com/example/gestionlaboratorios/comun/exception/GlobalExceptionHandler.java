package com.example.gestionlaboratorios.comun.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.gestionlaboratorios.comun.model.ApiResult;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
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

//     @ExceptionHandler(HttpMessageNotReadableException.class)
//     public ResponseEntity<ApiResult<String>> handleJsonParseError(HttpMessageNotReadableException ex) {
//         ApiResult<String> respuesta = new ApiResult<>(
//             "Error en el formato del json",
//              ex.getMostSpecificCause().getMessage(),
//              HttpStatus.BAD_REQUEST.value()
//          );

//         return ResponseEntity.badRequest().body(respuesta);
        
//     }
}
