package com.kaczmar.MicroLoanApp.service;

import com.kaczmar.MicroLoanApp.OwnExceptions.AmountNotInRangeException;
import com.kaczmar.MicroLoanApp.OwnExceptions.DateNotInRangeException;
import com.kaczmar.MicroLoanApp.OwnExceptions.TimeAndAmountException;
import com.kaczmar.MicroLoanApp.dto.LoanInput;
import com.kaczmar.MicroLoanApp.requirements.LoanRequirements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class MicroLoanService {

    public static final String AMOUNT_IS_NOT_IN_OUR_RANGE = "APPLICATION IS REJECTED AS AMOUNT IS NOT IN OUR RANGE";
    public static final String DATE_IS_NOT_IN_OUR_RANGE = "APPLICATION IS REJECTED AS DATE IS NOT IN OUR RANGE";
    public static final String FOR_MAX_AMOUNT_PLEASE_DO_IT_BETWEEN_06_00_24_00 = "YOU ARE REQUESTING FOR MAX AMOUNT, PLEASE DO IT BETWEEN 06:00 - 24:00";
    private final LoanRequirements loanRequirements;

    public MicroLoanService(LoanRequirements loanRequirements) {
        this.loanRequirements = loanRequirements;
    }

    public String applyForLoan(LoanInput loanInput) throws AmountNotInRangeException, DateNotInRangeException, TimeAndAmountException {
        BigDecimal amount = loanInput.getAmount();
        LocalDate date = LocalDate.parse(loanInput.getDate());
        LocalTime currentTime = LocalTime.now();

        isAmountValid(amount, loanRequirements);
        isTermValid(date, loanRequirements);
        isTimeValid(loanInput, currentTime, loanRequirements);

        return "We approve this Loan";

    }

    private void isTimeValid(LoanInput loanInput, LocalTime currentTime, LoanRequirements loanRequirements) throws TimeAndAmountException {
        if((!currentTime.isAfter(loanRequirements.getTimeMin()) || !currentTime.isBefore(loanRequirements.getTimeMax())) && loanInput.getAmount().compareTo(loanRequirements.getAmountMax()) == 0){
            throw new TimeAndAmountException(FOR_MAX_AMOUNT_PLEASE_DO_IT_BETWEEN_06_00_24_00);
        }
    }

    private void isTermValid(LocalDate date, LoanRequirements loanRequirements) throws DateNotInRangeException {
        if(!date.isAfter(loanRequirements.getTermMin()) || !date.isBefore(loanRequirements.getTermMax())){
            throw new DateNotInRangeException(DATE_IS_NOT_IN_OUR_RANGE);
        }
    }

    private void isAmountValid(BigDecimal amount, LoanRequirements loanRequirements) throws AmountNotInRangeException {
        if(!(amount.compareTo(loanRequirements.getAmountMin()) >= 0) || !(amount.compareTo(loanRequirements.getAmountMax()) <= 0)){
            throw new AmountNotInRangeException(AMOUNT_IS_NOT_IN_OUR_RANGE);
        }
    }


}
