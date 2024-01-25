package com.example.orderservice.Service;

import com.example.orderservice.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface OrderService {
    OrderApiResponse saveOrders(OrderRequest orderRequest) throws JsonProcessingException;

    OrderItemResponse findOrderByID(int id);

    List<SelectedData> orderIdDetails(int id);

    OrderIdResponse fullDetailsOfId(int id);

    List<OrderIdResponse> completeDetails();

    List<OrderIdResponse> allDetails();
}
