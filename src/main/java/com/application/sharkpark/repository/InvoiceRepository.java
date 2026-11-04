package com.application.sharkpark.repository;

import com.application.sharkpark.domain.Invoice;
import com.application.sharkpark.domain.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
//TODO:: TALK TO DICTATOR HARRY

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    Optional<Invoice> findInvoiceByUser_UserId(Integer userId);

}
