package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class products
{
    private int productId;
    private int quantity;
    private String name;
    //private int price;
    private int total;
}

