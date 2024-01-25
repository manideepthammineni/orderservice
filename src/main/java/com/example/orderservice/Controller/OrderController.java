package com.example.orderservice.Controller;

import com.example.orderservice.Service.OrderService;
import com.example.orderservice.Service.Producer;
import com.example.orderservice.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class OrderController
{
    @Autowired
     private OrderService orderService;

    @Autowired
    Producer producer;

    @PostMapping("/createOrder")
    public OrderApiResponse placeOrder(@RequestBody OrderRequest orderRequest) throws JsonProcessingException {
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

    @GetMapping("completeDetails")
    public List<OrderIdResponse> completeDetails()
    {
        return orderService.completeDetails();
    }

    @GetMapping("allDetails")
    public List<OrderIdResponse>allDetails()
    {
        return orderService.allDetails();
    }

   /* @GetMapping("/producerMessage")
    public void getMessageFromClient(@RequestParam("message") String message)
    {
        producer.sendMessageToTopic(message);
    }*/


}
