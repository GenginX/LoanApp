package com.kaczmar.MicroLoanApp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaczmar.MicroLoanApp.dto.LoanInput;
import com.kaczmar.MicroLoanApp.repository.MicroLoanRepository;
import com.kaczmar.MicroLoanApp.service.MicroLoanService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class MicroLoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MicroLoanService microLoanService;

    @Autowired
    private MicroLoanRepository microLoanRepository;

    @AfterEach
    void tearDown(){
        microLoanRepository.deleteAll();
    }

    @Test
    public void applyForLoan() throws Exception {
        //given
        LoanInput loanInput = new LoanInput(new BigDecimal(50000), "2030-12-19");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .post(MicroLoanController.LOAN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(loanInput));
        //when
        final ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);

        //test
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void extendLoan() throws Exception{
        //given
        LoanInput loanInput = new LoanInput(new BigDecimal(50000), "2030-12-19");
        microLoanService.applyForLoan(loanInput);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .post(MicroLoanController.LOAN + "/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        //when
        final ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);

        //test
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void applyForLoanUnHappyPathAmount() throws Exception{
        //given
        LoanInput loanInput = new LoanInput(new BigDecimal(1), "2030-12-19");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .post(MicroLoanController.LOAN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(loanInput));
        //when
        final ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);

        //test
        resultActions.andExpect(status().isNotAcceptable());
    }

    @Test
    public void applyForLoanUnHappyPathDate() throws Exception{
        //given
        LoanInput loanInput = new LoanInput(new BigDecimal(50000), "2024-12-19");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .post(MicroLoanController.LOAN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(loanInput));
        //when
        final ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);

        //test
        resultActions.andExpect(status().isNotAcceptable());
    }
    @Test
    public void applyForLoanUnHappyPathTimeAndAmount() throws Exception{
        //given
        LoanInput loanInput = new LoanInput(new BigDecimal(1000000), "2035-12-19");
        loanInput.setLocalTimeNow(LocalTime.of(3,0));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .post(MicroLoanController.LOAN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(loanInput));
        //when
        final ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);

        //test
        resultActions.andExpect(status().isNotAcceptable());
    }


    private static String toJson(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}