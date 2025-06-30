package com.example.demo.execption;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.ServletException;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
     String customMessage =  "Argument invalide fourni : " + ex.getMessage();
    return ResponseEntity.badRequest()
        .body(Map.of("error",customMessage));
  }


@ExceptionHandler(UsernameNotFoundException.class)
public ResponseEntity<Map<String, String>> handleUsernameNotFound(UsernameNotFoundException ex) {
     String customMessage = "Utilisateur non trouvé, accès refusé : " + ex.getMessage();
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                         .body(Map.of("error", customMessage));
}

@ExceptionHandler(Exception.class)
public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
     String customMessage = "Une erreur inattendue est survenue. Détails : " + ex.getMessage();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                         .body(Map.of("error", customMessage));
}



  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<Map<String, String>> handleResponseStatusException(
      ResponseStatusException ex) {
        String customMessage = "Une erreur est survenue : " + ex.getReason();
    return ResponseEntity.status(ex.getStatusCode())
                         .body(Map.of("error",customMessage));
  }



@ExceptionHandler(RuntimeException.class)
public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
     String customMessage = "Une erreur interne est survenue : " + ex.getMessage();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of("error", customMessage));
}

@ExceptionHandler(ServletException.class)
public ResponseEntity<Map<String, String>> handleServletException(ServletException ex) {
    String  customMessage="Une erreur technique liée au serveur est survenue : " + ex.getMessage();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                         .body(Map.of("error", customMessage));

}



}
