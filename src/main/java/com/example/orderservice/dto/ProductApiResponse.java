package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor@AllArgsConstructor@Data
public class ProductApiResponse {


    private Integer statusCode;
    private  boolean success;
    private String statusMessage;
    private List<Product> data;
}
