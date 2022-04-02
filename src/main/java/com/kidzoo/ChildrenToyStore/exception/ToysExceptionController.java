package com.kidzoo.ChildrenToyStore.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ToysExceptionController {

    @ExceptionHandler(value = ToysLimitException.class)
    public ResponseEntity<Object> toysLimitException(ToysLimitException toysLimitException) {
        return new ResponseEntity<Object>(toysLimitException.getMessage(),HttpStatus.valueOf(Integer.valueOf(toysLimitException.getStatusCode())));
    }

    @ExceptionHandler(value = IncorrectToyStatusException.class)
    public ResponseEntity<Object> incorrectToyStatusException(IncorrectToyStatusException incorrectToyStatusException) {
        return new ResponseEntity<Object>(incorrectToyStatusException.getMessage(),HttpStatus.valueOf(Integer.valueOf(incorrectToyStatusException.getStatusCode())));
    }

    @ExceptionHandler(value = ToyNotFoundException.class)
    public ResponseEntity<Object> toyNotFoundException(ToyNotFoundException toyNotFoundException) {
        return new ResponseEntity<Object>(toyNotFoundException.getMessage(),HttpStatus.valueOf(Integer.valueOf(toyNotFoundException.getStatusCode())));
    }

    @ExceptionHandler(value = NoToyFoundInPriceRangeException.class)
    public ResponseEntity<Object> toyNotFoundException(NoToyFoundInPriceRangeException noToyFoundInPriceRangeException) {
        return new ResponseEntity<Object>(noToyFoundInPriceRangeException.getMessage(),HttpStatus.valueOf(Integer.valueOf(noToyFoundInPriceRangeException.getStatusCode())));
    }


}
