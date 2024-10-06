package com.passcom.PassCom.exceptions;

import com.passcom.PassCom.dto.ErrorMessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorMessageDTO messageDTO = new ErrorMessageDTO(ex.getMessage());
        return new ResponseEntity<>(messageDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ErrorMessageDTO messageDTO = new ErrorMessageDTO(ex.getMessage());
        return new ResponseEntity<>(messageDTO, HttpStatus.CONFLICT);
    }
}
