package com.application.sharkpark.controller;

import com.application.sharkpark.dto.response.InvoiceResponse;
import com.application.sharkpark.enums.Feature;
import com.application.sharkpark.service.InvoiceService;
import com.application.sharkpark.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final Logger logger =  LoggerFactory.getLogger(InvoiceController.class);
    private SecurityService securityService;

    public InvoiceController(InvoiceService invoiceService, SecurityService securityService) {
        this.invoiceService = invoiceService;
        this.securityService = securityService;
    }


    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/{userId}", produces = "application/json")
    public InvoiceResponse getInvoiceByUserId(@PathVariable("userId") int userId,
                                              @RequestHeader(value = "Authorization", required = false) String authorization)
    {
        securityService.validateAuthorization(authorization, Feature.GET_INVOICE);

        logger.info("getting invoice by userId={}",userId);

        return invoiceService.getInvoiceByUserId(userId);
    }

}
