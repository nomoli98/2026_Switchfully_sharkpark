package com.application.sharkpark.repository;

import com.application.sharkpark.domain.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Integer> {
    //TODO getbyParkingAllocationByUserIdFilterStatusStopped(Integer UserId)
}
