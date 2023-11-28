package com.pblgllgs.service;
/*
 *
 * @author pblgl
 * Created on 23-11-2023
 *
 */

import com.pblgllgs.message.DispatchPreparing;
import com.pblgllgs.message.OrderCreated;
import com.pblgllgs.message.OrderDispatched;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DispatchService {

    private static final String DISPATCH_TRACKING_TOPIC = "dispatch.tracking";
    private static final String ORDER_DISPATCHED_TOPIC = "order.dispatched";
    private static final UUID APPLICATION_ID = UUID.randomUUID();

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void process(OrderCreated orderCreated) throws Exception {

        DispatchPreparing dispatchPreparing = DispatchPreparing.builder()
                .orderId(orderCreated.getOrderId())
                .build();
        kafkaTemplate.send(DISPATCH_TRACKING_TOPIC, dispatchPreparing).get();


        OrderDispatched orderDispatched = OrderDispatched.builder()
                .orderId(orderCreated.getOrderId())
                .processedById(APPLICATION_ID)
                .notes("Dispatched: "+ orderCreated.getItem())
                .build();
        kafkaTemplate.send(ORDER_DISPATCHED_TOPIC, orderDispatched).get();
        log.info("Sent messages: orderId: "+ orderCreated.getOrderId() + " - processedById: " + APPLICATION_ID);
    }
}
