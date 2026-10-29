package com.tecmilenio.saludimc.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.dao.DataIntegrityViolationException;
import java.time.LocalDateTime;

/** El mismo error se serializa como JSON o XML segun el encabezado Accept. */
@RestControllerAdvice(basePackages = "com.tecmilenio.saludimc.api")
public class ErroresApi {
    public record ErrorApi(int status, String error, String mensaje, String ruta, LocalDateTime fecha) {
    }

    private ResponseEntity<ErrorApi> respuesta(HttpStatusCode estado, String texto, HttpServletRequest req) {
        return ResponseEntity.status(estado).body(new ErrorApi(estado.value(), "Error en la solicitud", texto,
                req.getRequestURI(), LocalDateTime.now()));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorApi> estado(ResponseStatusException e, HttpServletRequest req) {
        return respuesta(e.getStatusCode(), e.getReason() == null ? "Solicitud rechazada" : e.getReason(), req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApi> validacion(MethodArgumentNotValidException e, HttpServletRequest req) {
        var detalle = e.getBindingResult().getFieldErrors().stream()
                .map(x -> x.getField() + ": " + x.getDefaultMessage()).findFirst().orElse("Datos no válidos");
        return respuesta(HttpStatus.BAD_REQUEST, detalle, req);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorApi> jsonIncorrecto(HttpMessageNotReadableException e, HttpServletRequest req) {
        return respuesta(HttpStatus.BAD_REQUEST, "Cuerpo de solicitud incorrecto", req);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorApi> duplicado(DataIntegrityViolationException e, HttpServletRequest req) {
        return respuesta(HttpStatus.CONFLICT, "Registro duplicado o relacionado con otros datos", req);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorApi> argumento(IllegalArgumentException e, HttpServletRequest req) {
        return respuesta(HttpStatus.BAD_REQUEST, e.getMessage(), req);
    }
}
