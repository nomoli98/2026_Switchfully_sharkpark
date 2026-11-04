package com.application.sharkpark.repository;

import com.application.sharkpark.domain.Invoice;
import com.application.sharkpark.domain.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    List<Invoice> getAllByUser_UserIdAndStatus_ClosedAndClosed_atIsBefore(
            Integer userId, InvoiceStatus status, LocalDateTime closedAt);


}
