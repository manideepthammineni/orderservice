package com.example.orderservice.Entity;

import com.example.orderservice.dto.ItemDetailsRequestDto;
import com.example.orderservice.dto.OrderRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items")
public class OrderItems
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int productId;
    @ManyToOne(cascade = CascadeType.ALL,targetEntity = Orders.class)
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    private Orders order;
    private int quantity;
    private int total;

    public OrderItems(ItemDetailsRequestDto itemDetailsRequestDto, Orders order)
    {
        this.productId=itemDetailsRequestDto.getProductId();
        this.quantity=itemDetailsRequestDto.getQuantity();
        this.order = order;
    }
}
