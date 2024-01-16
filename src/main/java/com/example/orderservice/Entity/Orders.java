package com.example.orderservice.Entity;


import com.example.orderservice.dto.OrderRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Orders
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int amount;
    @OneToMany( cascade = CascadeType.ALL,mappedBy = "order")
    private List<OrderItems> orderItems;
    public Orders(OrderRequest orderRequest,List<OrderItems> orderItems)
    {
        this.orderItems=orderItems;
    }
}
