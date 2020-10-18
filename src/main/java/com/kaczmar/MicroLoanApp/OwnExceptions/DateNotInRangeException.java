package com.kaczmar.MicroLoanApp.OwnExceptions;

public class DateNotInRangeException extends Exception{

    public DateNotInRangeException(String message) {
        super(message);
    }
}
