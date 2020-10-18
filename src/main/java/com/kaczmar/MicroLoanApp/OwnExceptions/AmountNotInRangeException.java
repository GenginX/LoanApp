package com.kaczmar.MicroLoanApp.OwnExceptions;

public class AmountNotInRangeException extends Exception {

    public AmountNotInRangeException(String message) {
        super(message);
    }
}
