package com.passcom.PassCom.exceptions;

import com.passcom.PassCom.dto.MessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        MessageDTO messageDTO = new MessageDTO(ex.getMessage());
        return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        MessageDTO messageDTO = new MessageDTO(ex.getMessage());
        return new ResponseEntity<>(messageDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        MessageDTO messageDTO = new MessageDTO(ex.getMessage());
        return new ResponseEntity<>(messageDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccentNotFoundException.class)
    public ResponseEntity<Object> handleAccentNotFoundException(AccentNotFoundException ex) {
        MessageDTO messageDTO = new MessageDTO(ex.getMessage());
        return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccentAlreadySoldException.class)
    public ResponseEntity<Object> handleAccentAlreadySoldException(AccentAlreadySoldException ex) {
        MessageDTO messageDTO = new MessageDTO(ex.getMessage());
        return new ResponseEntity<>(messageDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        MessageDTO messageDTO = new MessageDTO(ex.getMessage());
        return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
    }
}

