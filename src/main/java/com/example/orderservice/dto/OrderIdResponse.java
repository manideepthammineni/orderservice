package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderIdResponse
{
    private int id;
    private int amount;
    List<products> allProductsDetails;

    public OrderIdResponse(int id, int amount, List<products> allProductsDetails) {
        this.id = id;
        this.amount = amount;
        this.allProductsDetails = allProductsDetails;
    }
}
