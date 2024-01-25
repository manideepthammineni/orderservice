package com.example.orderservice.Repository;

import com.example.orderservice.Entity.Orders;

import com.example.orderservice.dto.OrderIdResponse;
import com.example.orderservice.dto.Product;
import com.example.orderservice.dto.SelectedData;
import com.example.orderservice.dto.SelectedProductsData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders,Integer>
{
    @Query(value = "select orders.id as id, orders.amount as amount , order_items.product_id as productId, order_items.quantity as Quantity,order_items.total as total, product.name as name from orders join order_items on orders.id = order_id join product on product.id = order_items.product_id where orders.id = :id",nativeQuery = true)
     List<SelectedData> orderIdDetails(Integer id);


    @Query(value = "select orders.id as id, orders.amount as amount , order_items.product_id as productId, order_items.quantity as Quantity,order_items.total as total, product.name as name from orders join order_items on orders.id = order_id join product on product.id = order_items.product_id",nativeQuery = true)
    List<SelectedData> completeDetails();


    @Query(value = "select id from orders",nativeQuery = true)
    List<Integer> ids();


}
