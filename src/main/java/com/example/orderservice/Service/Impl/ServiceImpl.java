package com.example.orderservice.Service.Impl;

import com.example.orderservice.Entity.OrderItems;
import com.example.orderservice.Entity.Orders;
import com.example.orderservice.Repository.OrderItemsRepository;
import com.example.orderservice.Repository.OrdersRepository;
import com.example.orderservice.Service.OrderService;
import com.example.orderservice.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServiceImpl implements OrderService
{
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    OrdersRepository orderRepository;
    @Autowired
    OrderItemsRepository orderItemsRepository;

    public OrderApiResponse validations(OrderRequest orderRequest)
    {
        for(ItemDetailsRequestDto itemDetailsRequestDto : orderRequest.getItemDetails())
        {
            if(itemDetailsRequestDto.getQuantity()==0 || itemDetailsRequestDto.getProductId()>=5)
                return new OrderApiResponse(202,false,"Data given is wrong",0);
        }
        return new OrderApiResponse(201,true,"Data successfully inserted in all tables",0);
    }

    public static List<Integer> givenProductIds(OrderRequest orderRequest)
    {
        List<Integer> ids = new ArrayList<>();
        for(ItemDetailsRequestDto itemDetailsRequestDto : orderRequest.getItemDetails())
        {
            ids.add(itemDetailsRequestDto.getProductId());
        }
        return ids;
    }

    public  Map<Integer,Product> allProductsData(OrderRequest orderRequest)
    {
        ProductApiResponse productApiResponse = restTemplate.postForObject(Link.url,new SelectedProductsData(givenProductIds(orderRequest)), ProductApiResponse.class);
        List<Product> products =  productApiResponse.getData();
        Map<Integer,Product> productMap=new HashMap<>();
        for (Product product:products)
        {
            productMap.put(product.getId(),product);
        }
        return productMap;

    }
    public OrderApiResponse saveOrders(OrderRequest orderRequest)
    {
        OrderApiResponse orderApiResponse = validations(orderRequest);
        if (!orderApiResponse.isSuccess())
            return orderApiResponse;

            int amount=0;
            List<OrderItems> orderItems = new ArrayList<>();
            Orders order = new Orders();
            Map<Integer,Product> mp = allProductsData(orderRequest);
            for (ItemDetailsRequestDto itemDetailsRequestDto: orderRequest.getItemDetails())
            {
                OrderItems o = new OrderItems();
                o.setProductId(itemDetailsRequestDto.getProductId());
                amount = amount + mp.get(itemDetailsRequestDto.getProductId()).getPrice()*itemDetailsRequestDto.getQuantity();
                o.setTotal(mp.get(itemDetailsRequestDto.getProductId()).getPrice()*itemDetailsRequestDto.getQuantity());
                o.setQuantity(itemDetailsRequestDto.getQuantity());
                o.setOrder(order);
                orderItems.add(o);
            }
            order.setAmount(amount);
            orderItemsRepository.saveAll(orderItems);

            return new OrderApiResponse(201,true,"Data successfully inserted in all tables",order.getAmount());


    }

    public OrderItemResponse findOrderByID(int id){
        Orders orders=orderRepository.findById(id).get();
        List<OrderItems> orderItems=orders.getOrderItems();
        List<ItemDetailsResponse> itemDetailsResponses=new ArrayList<>();
        RestTemplate restTemplate=new RestTemplate();
        for(OrderItems orderItems1:orderItems)
        {
            Object product=restTemplate.getForObject("http://localhost:9000/product/get?id="+orderItems1.getProductId(),Object.class);
            ItemDetailsResponse itemDetailsResponse=new ItemDetailsResponse(orderItems1,product);
            itemDetailsResponses.add(itemDetailsResponse);
        }
        OrderItemResponse orderItemResponse = new OrderItemResponse(itemDetailsResponses,orders);
        return orderItemResponse;
    }

    @Override
    public List<SelectedData> orderIdDetails(int id)
    {

        return orderRepository.orderIdDetails(id);
    }

    @Override
    public OrderIdResponse fullDetailsOfId(int id) {

        List<SelectedData> selectedData =  orderRepository.orderIdDetails(id);
        
        if(selectedData.isEmpty())
            return null;
        OrderIdResponse orderIdResponse = new OrderIdResponse();
        orderIdResponse.setId(selectedData.get(0).getId());
        List<products> p = new ArrayList<>();
        orderIdResponse.setAmount(selectedData.get(0).getAmount());
        selectedData.forEach(selectedData1 -> {
            products p1 = new products(selectedData1.getProductId(),selectedData1.getQuantity(),selectedData1.getName(),selectedData1.getTotal());
            p.add(p1);
        });
        orderIdResponse.setAllProductsDetails(p);
        return orderIdResponse;
    }


}
