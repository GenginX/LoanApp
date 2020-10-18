package com.kaczmar.MicroLoanApp.service;

import com.kaczmar.MicroLoanApp.exceptions.AmountNotInRangeException;
import com.kaczmar.MicroLoanApp.exceptions.DateNotInRangeException;
import com.kaczmar.MicroLoanApp.exceptions.TimeAndAmountException;
import com.kaczmar.MicroLoanApp.dto.LoanEntity;
import com.kaczmar.MicroLoanApp.dto.LoanInput;
import com.kaczmar.MicroLoanApp.repository.MicroLoanRepository;
import com.kaczmar.MicroLoanApp.requirements.LoanRequirements;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import org.junit.jupiter.api.function.Executable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class MicroLoanServiceTest {

    @Autowired
    MicroLoanService microLoanService;

    @Autowired
    MicroLoanRepository microLoanRepository;

    @Autowired
    LoanRequirements loanRequirements;

    @AfterEach
    void tearDown() {
        microLoanRepository.deleteAll();
    }

    @Test
    public void isApplyForLoanCreated() throws AmountNotInRangeException, TimeAndAmountException, DateNotInRangeException {
        //given
        LoanInput loanInput = new LoanInput(new BigDecimal(50000), "2030-12-19");
        LocalDate loanDateNow = LocalDate.now();
        LocalTime localTimeNow = LocalTime.now();
        //when
        microLoanService.applyForLoan(loanInput);
        //then
        List<LoanEntity> createdLoan = microLoanRepository.findAll();
        assertEquals(createdLoan.size(), 1);
        int resultOfDateOfRepaymentComparison = createdLoan.get(0).getDateOfRepayment().compareTo(LocalDate.parse(loanInput.getDate()));
        int resultOfDateComparison = createdLoan.get(0).getDateOfRequest().compareTo(loanDateNow);
        assertEquals(resultOfDateComparison, 0);
        assertEquals(resultOfDateOfRepaymentComparison, 0);
    }

    @Test
    @Transactional
    public void isLoanExtended() throws AmountNotInRangeException, TimeAndAmountException, DateNotInRangeException {
        //given
        LoanInput loanInput = new LoanInput(new BigDecimal(50000), "2030-12-19");
        LocalTime localTimeNow = LocalTime.now();
        microLoanService.applyForLoan(loanInput);
        List<LoanEntity> noExtendedLoans = microLoanRepository.findAll();
        BigDecimal totalCostOnBeggining = noExtendedLoans.get(0).getTotalCost();
        LocalDate dateOfRepaymentOnBeggining = noExtendedLoans.get(0).getDateOfRepayment();
        //when
        Long id = 2L;
        microLoanService.extendLaon(id);
        //then
        List<LoanEntity> loansAfterExtend = microLoanRepository.findAll();
        int resultOfTotalCostAfterLoanExtendsComparison = loansAfterExtend.get(0).getTotalCost().compareTo(totalCostOnBeggining);
        int resultOfDateAfterLoanExtendsComparison = loansAfterExtend.get(0).getDateOfRepayment().compareTo(dateOfRepaymentOnBeggining.plusYears(5L));
        assertEquals(resultOfTotalCostAfterLoanExtendsComparison, 0);
        assertEquals(resultOfDateAfterLoanExtendsComparison, 0);
    }

    @Test
    public void isApplyForLoanCreatedUnHappyPathAmount() {
        //given
        LoanInput loanInput = new LoanInput(new BigDecimal(1), "2030-12-19");
        LocalTime localTimeNow = LocalTime.now();
        //when
        Executable executable = () -> microLoanService.applyForLoan(loanInput);
        //then
        assertThrows(AmountNotInRangeException.class, executable);
    }

    @Test
    public void isApplyForLoanCreatedUnHappyPathDate() {
        //given
        LoanInput loanInput = new LoanInput(new BigDecimal(50000), "2002-12-19");
        //when
        Executable executable = () -> microLoanService.applyForLoan(loanInput);
        //then
        assertThrows(DateNotInRangeException.class, executable);
    }
    @Test
    public void isApplyForLoanCreatedUnHappyPAthDateTimeAndAmount(){
        //given
        LoanInput loanInput = new LoanInput(new BigDecimal(1000000), "2035-12-19");
        loanInput.setLocalTimeNow(LocalTime.of(3,0));
        //when
        Executable executable = () -> microLoanService.applyForLoan(loanInput);
        //then
        assertThrows(TimeAndAmountException.class, executable);
    }
}