package com.application.sharkpark.mapper;

import com.application.sharkpark.domain.Invoice;
import com.application.sharkpark.dto.response.InvoiceResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@Component
public class InvoiceMapper {

    public InvoiceResponse mapToInvoiceResponse(Invoice invoice, BigDecimal totalPrice) {
        return new InvoiceResponse(
                invoice.getInvoiceId(),
                invoice.getCreatedAt(),
                invoice.getExpiredAt(),
                invoice.getStatus().toString(),
                invoice.getClosed_at(),
                totalPrice
        );
    }

}
