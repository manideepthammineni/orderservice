package com.example.orderservice.dto;

import com.example.orderservice.Entity.OrderItems;
import com.example.orderservice.Entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor@AllArgsConstructor@Data
public class OrderItemResponse {
    Integer orderid;
    Integer amount;
    List<ItemDetailsResponse> itemDetailsResponses;
    public OrderItemResponse(List<ItemDetailsResponse> itemDetailsResponse, Orders orders)
    {
        this.itemDetailsResponses=itemDetailsResponse;
        this.orderid=orders.getId();
        this.amount=orders.getAmount();
    }
}
