package com.kaczmar.MicroLoanApp.requirements;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@Component
public class LoanRequirements {

    @Value("${amount.min}")
    private Integer amountMin;

    @Value("${amount.max}")
    private Integer amountMax;

    @Value("${term.min_year_from_now}")
    private Integer termMin;

    @Value("${term.max_year_from_now}")
    private Integer termMax;

    @Value("${time.max}")
    private Integer timeMax;

    @Value("${time.min}")
    private Integer timeMin;

    public BigDecimal getAmountMin(){
        return new BigDecimal(this.amountMin);
    }

    public BigDecimal getAmountMax(){
        return new BigDecimal(this.amountMax);
    }

    public LocalDate getTermMin(){
        return LocalDate.now().plusYears(termMin.longValue());
    }

    public LocalDate getTermMax(){
        return LocalDate.now().plusYears(termMax.longValue());
    }

    public LocalTime getTimeMax(){
        return LocalTime.of(timeMax, 0);
    }

    public LocalTime getTimeMin(){
        return LocalTime.of(timeMin, 0);
    }


}
