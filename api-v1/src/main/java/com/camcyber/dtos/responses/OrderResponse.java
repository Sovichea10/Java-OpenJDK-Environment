package com.camcyber.dtos.responses;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {

    private Integer id;
    private String receiptNumber;
    private double totalDollar;
//    private double totalKhmer;
    private double amountDollar;
//    private double amountKhmer;
    private double cashReturn;
    private Integer cashierId;
    private String cashierName;
    private LocalDateTime receiptDate;
    private List<ProductOrderResponse> products;


}
