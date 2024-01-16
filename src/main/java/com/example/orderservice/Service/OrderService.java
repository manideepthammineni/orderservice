package com.example.orderservice.Service;

import com.example.orderservice.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    OrderApiResponse saveOrders(OrderRequest orderRequest);

    OrderItemResponse findOrderByID(int id);

    List<SelectedData> orderIdDetails(int id);

    OrderIdResponse fullDetailsOfId(int id);
}
