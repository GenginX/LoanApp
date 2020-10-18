package com.kaczmar.MicroLoanApp.repository;

import com.kaczmar.MicroLoanApp.dto.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MicroLoanRepository extends JpaRepository<LoanEntity, Long> {
}
