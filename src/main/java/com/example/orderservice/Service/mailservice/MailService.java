package com.example.orderservice.Service.mailservice;

import com.example.orderservice.Controller.OrderController;
import com.example.orderservice.Repository.OrdersRepository;
import com.example.orderservice.Service.OrderService;
import com.example.orderservice.dto.OrderIdResponse;
import com.example.orderservice.dto.Product;
import com.example.orderservice.dto.SelectedData;
import com.example.orderservice.dto.products;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class MailService
{

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    TemplateEngine templateEngine;


    @Autowired
     private OrdersRepository orderRepository;

    public List<products> detailsOfProducts(int id)
    {
        List<SelectedData> selectedData =  orderRepository.orderIdDetails(id);
        List<products> p = new ArrayList<>();
        selectedData.forEach(selectedData1 -> {
            products p1 = new products(selectedData1.getProductId(),selectedData1.getQuantity(),selectedData1.getName(),selectedData1.getTotal());
            p.add(p1);
        });
        return p;
    }

    public OrderIdResponse allDetails(int id) {

        List<SelectedData> selectedData =  orderRepository.orderIdDetails(id);
        if(selectedData.isEmpty())
            return null;
        OrderIdResponse orderIdResponse = new OrderIdResponse();
        orderIdResponse.setId(selectedData.get(0).getId());
        orderIdResponse.setAmount(selectedData.get(0).getAmount());
        List<products> p = new ArrayList<>();
        selectedData.forEach(selectedData1 -> {
            products p1 = new products(selectedData1.getProductId(),selectedData1.getQuantity(),selectedData1.getName(),selectedData1.getTotal());
            p.add(p1);
        });
        orderIdResponse.setAllProductsDetails(p);

        return orderIdResponse;
    }

    public void sendMail(String info)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("manideept.robocamp@gmail.com");
        message.setTo("manideept.robocamp@gmail.com");
        message.setSubject("subject");
        message.setText(info);
        mailSender.send(message);
    }

    public void sendSimpleEmail(int id)
    {
        OrderIdResponse response = new OrderIdResponse();
        response = allDetails(id);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("manideept.robocamp@gmail.com");
        message.setTo("manideept.robocamp@gmail.com");
        message.setSubject("subject");
        message.setText(response.toString());

        mailSender.send(message);
        System.out.println("mail sent successfully");
    }



    public void table(int id){
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message=new MimeMessageHelper(mimeMessage,"UTF-8");
            message.setTo("manideept.robocamp@gmail.com");
            message.setSubject("Order Id details");
            OrderIdResponse response = new OrderIdResponse();
            response = allDetails(id);
            List<products> info = detailsOfProducts(id);

            Context context=new Context();
            context.setVariable("response",response);
            String datas=templateEngine.process("details",context);

            context.setVariable("info",info);
            String data=templateEngine.process("details",context);
            message.setText(data,true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

