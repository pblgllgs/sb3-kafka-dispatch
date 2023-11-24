package com.pblgllgs.service;
/*
 *
 * @author pblgl
 * Created on 23-11-2023
 *
 */

import com.pblgllgs.message.OrderCreated;
import com.pblgllgs.message.OrderDispatched;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class DispatchService {

    private final static String ORDER_DISPATCHED_TOPIC= "order.dispatched";

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public void process(OrderCreated orderCreated) throws Exception {
        OrderDispatched orderDispatched =  OrderDispatched.builder()
                .orderId(orderCreated.getOrderId())
                .build();
        kafkaTemplate.send(ORDER_DISPATCHED_TOPIC,orderDispatched).get();
    }
}
