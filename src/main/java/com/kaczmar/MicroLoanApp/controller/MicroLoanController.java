package com.kaczmar.MicroLoanApp.controller;

import com.kaczmar.MicroLoanApp.OwnExceptions.AmountNotInRangeException;
import com.kaczmar.MicroLoanApp.OwnExceptions.DateNotInRangeException;
import com.kaczmar.MicroLoanApp.OwnExceptions.TimeAndAmountException;
import com.kaczmar.MicroLoanApp.dto.LoanInput;
import com.kaczmar.MicroLoanApp.service.MicroLoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
public class MicroLoanController {

    private MicroLoanService microLoanService;

    public MicroLoanController(MicroLoanService microLoanService) {
        this.microLoanService = microLoanService;
    }

    @PostMapping
    private ResponseEntity<String> applyForLoan(@RequestBody LoanInput loan) throws AmountNotInRangeException, TimeAndAmountException, DateNotInRangeException {
        String s = microLoanService.applyForLoan(loan);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(s);
    }


}
