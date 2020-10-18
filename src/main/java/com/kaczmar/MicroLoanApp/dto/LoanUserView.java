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

    private LocalDate dateOfRepayment;

    private BigDecimal requestedAmount;

    private BigDecimal totalCost;

    private Period period;

    @Override
    public String toString() {
        return "\n dateOfRequest=" + dateOfRequest +
                "\n dateOfRepayment" + dateOfRepayment +
                "\n requestedAmount=" + requestedAmount +
                "\n totalCost=" + totalCost +
                "\n period=" + period.getYears() + " years, " + period.getMonths() + " months, " + period.getDays() + " days";
    }
}
