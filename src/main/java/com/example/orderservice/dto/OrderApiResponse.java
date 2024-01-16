package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderApiResponse
{
    private Integer statusCode;
    private  boolean success;
    private String statusMessage;
    private int totalAmount;


}
