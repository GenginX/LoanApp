package com.kaczmar.MicroLoanApp.controller;

import com.kaczmar.MicroLoanApp.OwnExceptions.AmountNotInRangeException;
import com.kaczmar.MicroLoanApp.OwnExceptions.DateNotInRangeException;
import com.kaczmar.MicroLoanApp.OwnExceptions.TimeAndAmountException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class MicroLoanAdvice {

    @ExceptionHandler(AmountNotInRangeException.class)
    public ResponseEntity<String> handleAmountNotInRangeException(AmountNotInRangeException e){
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(e.getMessage());
    }

    @ExceptionHandler(DateNotInRangeException.class)
    public ResponseEntity<String> handleDateNotInRangeException(DateNotInRangeException e){
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(e.getMessage());
    }

    @ExceptionHandler(TimeAndAmountException.class)
    public ResponseEntity<String> handleTimeAndAmountException(TimeAndAmountException e){
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(e.getMessage());
    }

}
