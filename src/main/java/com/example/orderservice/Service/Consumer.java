package com.example.orderservice.Service;

import com.example.orderservice.Service.mailservice.MailService;
import com.example.orderservice.dto.OrderIdResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service

public class Consumer
{

    @Autowired
    MailService mailService;
    @KafkaListener(topics = "OrderIdDetails",groupId = "group")
    public  void listenToTopic(String receivedMessage)
    {
      try
      {
          //System.out.println("The message received is " + receivedMessage);

          ObjectMapper mapper = new ObjectMapper();
          OrderIdResponse oid = mapper.readValue(receivedMessage, OrderIdResponse.class);

          mailService.table(oid.getId());
      }

    catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

}

