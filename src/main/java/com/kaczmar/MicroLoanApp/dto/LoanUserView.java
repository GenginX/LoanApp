package com.kaczmar.MicroLoanApp.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Data
@Builder
public class LoanUserView {

    private LocalDate dateOfRequest;

    private BigDecimal requestedAmount;

    private BigDecimal totalCost;

    private Period period;

    @Override
    public String toString() {
        return "dateOfRequest=" + dateOfRequest +
                "\n requestedAmount=" + requestedAmount +
                "\n totalCost=" + totalCost +
                "\n period=" + period.getYears() + " years, " + period.getMonths() + " months, " + period.getDays() + " days";
    }
}
