package com.application.sharkpark.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public record InvoiceResponse(
        Integer invoiceId,
        LocalDateTime createdAt,
        LocalDateTime expiredAt,
        String status,
        LocalDateTime paymentAt,
        BigDecimal totalPrice
        ) {


}
