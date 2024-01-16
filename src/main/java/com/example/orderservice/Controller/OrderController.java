package com.example.orderservice.Controller;

import com.example.orderservice.Service.OrderService;
import com.example.orderservice.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController
{
    @Autowired
     private OrderService orderService;

    @PostMapping("/createOrder")
    public OrderApiResponse placeOrder(@RequestBody OrderRequest orderRequest)
    {
        return orderService.saveOrders(orderRequest);
    }

   @GetMapping("/findOrder")
    public OrderItemResponse findOrder(@RequestParam(value = "id") int id)
    {
        return orderService.findOrderByID(id);
    }
    @GetMapping("/orderIdDetails")
    public List<SelectedData> orderDetails(@RequestParam(value = "id") int id)
    {
        return orderService.orderIdDetails(id);
    }

    @GetMapping("fullDetails")
    public OrderIdResponse fullDetails(@RequestParam(value = "id") int id)
    {
        return orderService.fullDetailsOfId(id);
    }


}
