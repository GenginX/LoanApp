package com.kaczmar.MicroLoanApp.service;

import com.kaczmar.MicroLoanApp.OwnExceptions.AmountNotInRangeException;
import com.kaczmar.MicroLoanApp.OwnExceptions.DateNotInRangeException;
import com.kaczmar.MicroLoanApp.OwnExceptions.TimeAndAmountException;
import com.kaczmar.MicroLoanApp.dto.LoanEntity;
import com.kaczmar.MicroLoanApp.dto.LoanInput;
import com.kaczmar.MicroLoanApp.dto.LoanUserView;
import com.kaczmar.MicroLoanApp.repository.MicroLoanRepository;
import com.kaczmar.MicroLoanApp.requirements.LoanRequirements;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;

@Service
public class MicroLoanService {



    public static final String AMOUNT_IS_NOT_IN_OUR_RANGE = "APPLICATION IS REJECTED AS AMOUNT IS NOT IN OUR RANGE";
    public static final String DATE_IS_NOT_IN_OUR_RANGE = "APPLICATION IS REJECTED AS DATE IS NOT IN OUR RANGE";
    public static final String FOR_MAX_AMOUNT_PLEASE_DO_IT_BETWEEN_06_00_24_00 = "YOU ARE REQUESTING FOR MAX AMOUNT, PLEASE DO IT BETWEEN 06:00 - 24:00";
    private final MicroLoanRepository microLoanRepository;
    private final LoanRequirements loanRequirements;

    public MicroLoanService(MicroLoanRepository microLoanRepository, LoanRequirements loanRequirements) {
        this.microLoanRepository = microLoanRepository;
        this.loanRequirements = loanRequirements;
    }

    public String applyForLoan(LoanInput loanInput) throws AmountNotInRangeException, DateNotInRangeException, TimeAndAmountException {
        BigDecimal amount = loanInput.getAmount();
        LocalDate requestedDateOfRepayment = LocalDate.parse(loanInput.getDate());
        LocalTime currentTime = LocalTime.now();

        validationAmountTermTime(loanInput, amount, requestedDateOfRepayment, currentTime);

        BigDecimal totalCost = principalCalculation(amount, loanRequirements).add(amount);
        Period period = repaymentPeriod(requestedDateOfRepayment);

        LoanEntity entityInDB = saveEntityInDB(amount, requestedDateOfRepayment, totalCost, period);
        LoanUserView userView = createUserView(entityInDB);

        return createResponse(userView);

    }

    private LoanUserView createUserView(LoanEntity entityInDB) {
        return LoanUserView.builder()
                .dateOfRequest(entityInDB.getDateOfRequest())
                .dateOfRepayment(entityInDB.getDateOfRepayment())
                .period(entityInDB.getPeriod())
                .requestedAmount(entityInDB.getRequestedAmount())
                .totalCost(entityInDB.getTotalCost())
                .build();

    }

    private LoanEntity saveEntityInDB(BigDecimal amount, LocalDate date, BigDecimal totalCost, Period period) {
        LoanEntity loanEntity = LoanEntity.builder()
                .dateOfRepayment(date)
                .dateOfRequest(LocalDate.now())
                .requestedAmount(amount)
                .totalCost(totalCost)
                .period(period)
                .build();
        microLoanRepository.save(loanEntity);
        return loanEntity;

    }

    private String createResponse(LoanUserView userView) {
        return "We approve this Loan:\n" + userView.toString();
    }

    private void validationAmountTermTime(LoanInput loanInput, BigDecimal amount, LocalDate date, LocalTime currentTime) throws AmountNotInRangeException, DateNotInRangeException, TimeAndAmountException {
        isAmountValid(amount, loanRequirements);
        isTermValid(date, loanRequirements);
        isTimeValid(loanInput, currentTime, loanRequirements);
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

    private BigDecimal principalCalculation(BigDecimal amount, LoanRequirements loanRequirements){
        return amount.multiply(loanRequirements.getPrincipals()).divide(BigDecimal.valueOf(100), RoundingMode.UP);
    }

    private Period repaymentPeriod(LocalDate providedDate){
        return Period.between(LocalDate.now(), providedDate);
    }

    public String extendLaon(Long id) throws DateNotInRangeException {
        LoanEntity one = microLoanRepository.getOne(id);
        LocalDate newDateOfRepayment = one.getDateOfRepayment().plusYears(loanRequirements.getMaxYearExtension());

        isTermValid(newDateOfRepayment, loanRequirements);

        LoanEntity loanExtended = updateLoan(one, newDateOfRepayment);
        LoanUserView userView = createUserView(loanExtended);

        return createResponse(userView);

    }

    private LoanEntity updateLoan(LoanEntity one, LocalDate newDateOfRepayment) {
        Period newPeriod = repaymentPeriod(newDateOfRepayment);
        one.setDateOfRepayment(newDateOfRepayment);
        one.setPeriod(newPeriod);
        microLoanRepository.save(one);
        return one;
    }


}
