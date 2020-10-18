package com.kaczmar.MicroLoanApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;


@Data
public class LoanInput {

    private BigDecimal amount;

    private String date;

    private LocalTime localTimeNow;

    public LoanInput(BigDecimal amount, String date) {
        this.amount = amount;
        this.date = date;
        this.localTimeNow = LocalTime.now();
    }

    public LocalTime getLocalTimeNow() {
        return localTimeNow;
    }

    public void setLocalTimeNow(LocalTime localTimeNow) {
        this.localTimeNow = localTimeNow;
    }
}
