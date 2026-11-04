package com.application.sharkpark.repository;

import com.application.sharkpark.domain.Invoice;
import com.application.sharkpark.domain.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {


}
