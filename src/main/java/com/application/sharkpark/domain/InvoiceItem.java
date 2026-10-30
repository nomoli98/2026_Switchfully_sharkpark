package com.application.sharkpark.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_items")
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_item_id")
    private Integer invoiceItemId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_parking_allocation_id")
    private ParkingAllocation parkingAllocation;

    @Column(name = "item_price", precision = 10, scale = 2)
    private BigDecimal itemPrice;

    @ManyToOne
    @JoinColumn(name = "fk_invoice_id")
    private Invoice invoiceId;

    public InvoiceItem() {
    }

    public Integer getInvoiceItemId() {
        return invoiceItemId;
    }

    public void setInvoiceItemId(Integer invoiceItemId) {
        this.invoiceItemId = invoiceItemId;
    }

    public ParkingAllocation getParkingAllocation() {
        return parkingAllocation;
    }

    public void setParkingAllocation(ParkingAllocation parkingAllocation) {
        this.parkingAllocation = parkingAllocation;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Invoice getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Invoice invoiceId) {
        this.invoiceId = invoiceId;
    }
}
