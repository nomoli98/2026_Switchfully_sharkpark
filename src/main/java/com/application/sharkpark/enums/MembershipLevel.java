package com.application.sharkpark.enums;

import java.math.BigDecimal;
import java.time.Duration;

public enum MembershipLevel {
    BRONZE(new BigDecimal("0"),  0, Duration.ofHours(4)),
    SILVER(new BigDecimal("10"), 20, Duration.ofHours(6)),
    GOLD(new BigDecimal("40"),   30, Duration.ofHours(24));

    private final BigDecimal monthlyCostInEuro;
    private final int hourlyPriceReductionPercentage;
    private final Duration maxAllocationTime;

    MembershipLevel(BigDecimal monthlyCostInEuro,
                    int hourlyPriceReductionPercentage,
                    Duration maxAllocationTime) {
        this.monthlyCostInEuro = monthlyCostInEuro;
        this.hourlyPriceReductionPercentage = hourlyPriceReductionPercentage;
        this.maxAllocationTime = maxAllocationTime;
    }

    public BigDecimal getMonthlyCostInEuro() { return monthlyCostInEuro; }
    public int getHourlyPriceReductionPercentage() { return hourlyPriceReductionPercentage; }
    public Duration getMaxAllocationTime() { return maxAllocationTime; }

    public BigDecimal applyReduction(BigDecimal basePricePerHour) {
        BigDecimal factor = BigDecimal.valueOf(100 - hourlyPriceReductionPercentage)
                                      .movePointLeft(2);
        return basePricePerHour.multiply(factor);
    }
}