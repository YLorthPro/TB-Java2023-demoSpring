package be.bstorm.formation.pl.rest.controller;

import be.bstorm.formation.bll.models.exception.NotFoundException;
import be.bstorm.formation.pl.models.Error;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestControllerAdvisor {
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Error> handleNotFoundException(NotFoundException exception, HttpServletRequest req){
        
        Error error = Error.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .requestMadeAt(LocalDateTime.now())
                .URI(req.getRequestURI())
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error);
    }
}
