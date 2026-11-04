package com.application.sharkpark.service;

import com.application.sharkpark.domain.Invoice;
import com.application.sharkpark.dto.response.InvoiceResponse;
import com.application.sharkpark.exceptions.NotFoundException;
import com.application.sharkpark.mapper.InvoiceMapper;
import com.application.sharkpark.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    public InvoiceService(InvoiceRepository invoiceRepository,  InvoiceMapper invoiceMapper) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
    }

    public InvoiceResponse getInvoiceByUserId(int userId) {
        Invoice invoice = invoiceRepository.findInvoiceByUser_UserId(userId).orElseThrow(() -> new NotFoundException("invoice not found"));

        BigDecimal totalPrice = invoice.getTotalPrice();

       return invoiceMapper.mapToInvoiceResponse(invoice, totalPrice);
    }
}
