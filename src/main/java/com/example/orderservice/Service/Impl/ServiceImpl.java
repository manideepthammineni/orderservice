package com.example.orderservice.Service.Impl;

import com.example.orderservice.Entity.OrderItems;
import com.example.orderservice.Entity.Orders;
import com.example.orderservice.Repository.OrderItemsRepository;
import com.example.orderservice.Repository.OrdersRepository;
import com.example.orderservice.Service.OrderService;
import com.example.orderservice.Service.Producer;
import com.example.orderservice.Service.mailservice.MailService;
import com.example.orderservice.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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

    @Autowired
    MailService mailService;

    @Autowired
    Producer producer;

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
       try
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
           //mailService.table(orderItems.get(0).getOrder().getId());

           OrderIdResponse orderIdResponse =  fullDetailsOfId(orderItems.get(0).getOrder().getId());

           ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
           String json = ow.writeValueAsString(orderIdResponse);
           producer.sendMessageToTopic(json);
           return new OrderApiResponse(201,true,"Data successfully inserted in all tables",order.getAmount());
       }
       catch(Exception e)
       {
           System.out.println(e.getMessage());
       }
        return null;
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
        List<products> p = new ArrayList<>();
        orderIdResponse.setId(selectedData.get(0).getId());
        orderIdResponse.setAmount(selectedData.get(0).getAmount());
        selectedData.forEach(selectedData1 -> {
            products p1 = new products(selectedData1.getProductId(),selectedData1.getQuantity(),selectedData1.getName(),selectedData1.getTotal());
            p.add(p1);
        });
        orderIdResponse.setAllProductsDetails(p);
        //mailService.sendSimpleEmail(id);
        // mailService.table(id);
        return orderIdResponse;
    }


    @Override
    public List<OrderIdResponse> completeDetails()
    {
        List<Integer> ids = orderRepository.ids();
        int count = ids.size();
        Map<Integer,OrderIdResponse> mp = new HashMap<>();
        for(int i=0;i<count;i++)
        {
            List<SelectedData> selectedData =  orderRepository.orderIdDetails(ids.get(i));
            OrderIdResponse orderIdResponse = new OrderIdResponse();
            List<products> p = new ArrayList<>();
            orderIdResponse.setId(selectedData.get(0).getId());
            orderIdResponse.setAmount(selectedData.get(0).getAmount());
            selectedData.forEach(selectedData1 -> {
                products p1 = new products(selectedData1.getProductId(),selectedData1.getQuantity(),selectedData1.getName(),selectedData1.getTotal());
                p.add(p1);
            });
            orderIdResponse.setAllProductsDetails(p);
            mp.put(ids.get(i),orderIdResponse );
        }
        List<OrderIdResponse> oid = new ArrayList<>();
        for(int i=0;i<count;i++)
        {
            oid.add(mp.get(ids.get(i)));
        }
        return oid;
    }


    public List<OrderIdResponse> allDetails()
    {
        List<SelectedData> selectedData = orderRepository.completeDetails();
        Map<Integer,List<SelectedData>> mp = selectedData.stream().collect(Collectors.groupingBy(data -> data.getId()));
        List<OrderIdResponse> oid = new ArrayList<>();

        for(Map.Entry<Integer, List<SelectedData>> itr : mp.entrySet())
        {
            OrderIdResponse orderIdResponse = new OrderIdResponse();
            List<products> p = new ArrayList<>();
            orderIdResponse.setId(itr.getKey());
            orderIdResponse.setAmount(itr.getValue().get(0).getAmount());
            itr.getValue().forEach(info -> {
                products p1 = new products(info.getProductId(), info.getQuantity(),info.getName(), info.getTotal());
                p.add(p1);
            });
            orderIdResponse.setAllProductsDetails(p);
            oid.add(orderIdResponse);
        }
        return oid;
    }



}
