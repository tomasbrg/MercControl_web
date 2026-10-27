package br.com.merccontrol.web;
import org.springframework.http.*;import org.springframework.web.bind.annotation.*;import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataIntegrityViolationException;import java.util.*;
@RestControllerAdvice public class Erros {
 @ExceptionHandler({IllegalArgumentException.class,MethodArgumentNotValidException.class,MethodArgumentTypeMismatchException.class,DataIntegrityViolationException.class}) public ResponseEntity<Map<String,String>> entrada(Exception ex){String mensagem=ex instanceof MethodArgumentNotValidException?"Preencha os campos corretamente.":ex instanceof DataIntegrityViolationException?"Registro duplicado ou relacionado a outros dados.":ex.getMessage();return ResponseEntity.badRequest().body(Map.of("erro",mensagem==null?"Dados inválidos.":mensagem));}
 @ExceptionHandler(NoSuchElementException.class) public ResponseEntity<Map<String,String>> naoEncontrado(Exception ex){return ResponseEntity.status(404).body(Map.of("erro",ex.getMessage()));}
}
