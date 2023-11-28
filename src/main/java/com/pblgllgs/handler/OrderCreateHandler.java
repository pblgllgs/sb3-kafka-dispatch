package com.pblgllgs.handler;
/*
 *
 * @author pblgl
 * Created on 23-11-2023
 *
 */

import com.pblgllgs.message.OrderCreated;
import com.pblgllgs.service.DispatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCreateHandler {

    private final DispatchService dispatchService;

    @KafkaListener(
            id = "orderConsumerClient",
            topics = "order.created",
            groupId = "dispatch.order.created.consumer",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            OrderCreated payload) {
        log.info("Received message: partition: " + partition + " - key: " + key + " - payload: " + payload.toString());
        try {
            dispatchService.process(key,payload);
        } catch (Exception e) {
            log.info("Process failure", e);
        }
    }
}
