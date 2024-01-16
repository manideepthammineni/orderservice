package com.example.orderservice.dto;

import com.example.orderservice.Entity.OrderItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor@AllArgsConstructor@Data
public class ItemDetailsResponse {
    int itemId;
    int total;
    int quantity;
    Object product;
    public ItemDetailsResponse(OrderItems orderItems, Object product){
        this.itemId=orderItems.getId();
        this.total=orderItems.getTotal();
        this.quantity=orderItems.getQuantity();
        this.product=product;
    }

}
