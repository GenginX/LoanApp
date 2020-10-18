package com.kaczmar.MicroLoanApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
public class LoanInput {

    private BigDecimal amount;

    private String date;

}
