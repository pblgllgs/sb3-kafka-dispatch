package com.pblgllgs.tracking.handlet;
/*
 *
 * @author pblgl
 * Created on 28-11-2023
 *
 */

import com.pblgllgs.message.DispatchPreparing;
import com.pblgllgs.service.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(
        id = "dispatchTrackingConsumerClient",
        topics = "dispatch.tracking",
        groupId = "tracking.dispatch.tracking",
        containerFactory = "kafkaListenerContainerFactory"
)
public class DispatchTrackingHandler {

    private final TrackingService trackingService;

    @KafkaHandler
    public void listen(DispatchPreparing dispatchPreparing) throws Exception {
        try {
            trackingService.process(dispatchPreparing);
        } catch (Exception e) {
            log.error("Processing failure", e);
        }
    }
}
