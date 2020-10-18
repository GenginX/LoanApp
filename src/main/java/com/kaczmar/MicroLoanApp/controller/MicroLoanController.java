package com.kaczmar.MicroLoanApp.controller;

import com.kaczmar.MicroLoanApp.exceptions.AmountNotInRangeException;
import com.kaczmar.MicroLoanApp.exceptions.DateNotInRangeException;
import com.kaczmar.MicroLoanApp.exceptions.TimeAndAmountException;
import com.kaczmar.MicroLoanApp.dto.LoanInput;
import com.kaczmar.MicroLoanApp.service.MicroLoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(MicroLoanController.LOAN)
public class MicroLoanController {

    public static final String LOAN = "/loan";
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

    @PostMapping("/{id}")
    private ResponseEntity<String> extendLoan(@PathVariable("id") Long id) throws DateNotInRangeException {
        String s = microLoanService.extendLaon(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(s);
    }


}
