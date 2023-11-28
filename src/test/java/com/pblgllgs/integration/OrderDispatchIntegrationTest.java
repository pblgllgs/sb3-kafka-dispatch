package com.pblgllgs.integration;
/*
 *
 * @author pblgl
 * Created on 24-11-2023
 *
 */

import com.pblgllgs.DispatchConfiguration;
import com.pblgllgs.message.DispatchPreparing;
import com.pblgllgs.message.OrderCreated;
import com.pblgllgs.message.OrderDispatched;
import com.pblgllgs.util.TestEventData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.UUID.randomUUID;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(classes = {DispatchConfiguration.class})
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@EmbeddedKafka(controlledShutdown = true)
@Slf4j
public class OrderDispatchIntegrationTest {

    private static final String ORDER_CREATED_TOPIC = "order.created";
    private static final String ORDER_DISPATCHED_TOPIC = "order.dispatched";
    private static final String DISPATCH_TRACKING_TOPIC = "dispatch.tracking";

    @Autowired
    private KafkaTestListener testListener;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Configuration
    static class TestConfig {
        @Bean
        public KafkaTestListener testListener() {
            return new KafkaTestListener();
        }
    }

    public static class KafkaTestListener {
        AtomicInteger dispatchPreparingCounter = new AtomicInteger(0);
        AtomicInteger orderDispatchCounter = new AtomicInteger(0);

        @KafkaListener(groupId = "KafkaIntegrationTest", topics = DISPATCH_TRACKING_TOPIC)
        void receiveDispatchPreparing(
                @Header(KafkaHeaders.RECEIVED_KEY) String key,
                @Payload DispatchPreparing payload
        ) {
            log.debug("Received DispatchPreparing key: " + key + " - payload " + payload);
            assertThat(key, notNullValue());
            assertThat(payload, notNullValue());
            dispatchPreparingCounter.incrementAndGet();
        }

        @KafkaListener(groupId = "KafkaIntegrationTest", topics = ORDER_DISPATCHED_TOPIC)
        void receiveDispatchPreparing(
                @Header(KafkaHeaders.RECEIVED_KEY) String key,
                @Payload OrderDispatched payload) {
            log.debug("Received OrderDispatched key: " + key + " - payload " + payload);
            assertThat(key, notNullValue());
            assertThat(payload, notNullValue());
            orderDispatchCounter.incrementAndGet();
        }
    }

    @BeforeEach
    public void setUp() {
        testListener.dispatchPreparingCounter.set(0);
        testListener.orderDispatchCounter.set(0);

        registry.getListenerContainers().forEach(container ->
                ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic()));
    }

    @Test
    void testOrderDispatchFlow() throws Exception {
        OrderCreated orderCreated = TestEventData.builderOrderCreatedEvent(randomUUID(), "my-item");
        String key = randomUUID().toString();
        sendMessage(ORDER_CREATED_TOPIC, key,orderCreated);

        await().atMost(3, TimeUnit.SECONDS).pollDelay(100, TimeUnit.MILLISECONDS)
                .until(testListener.dispatchPreparingCounter::get, equalTo(1));
        await().atMost(1, TimeUnit.SECONDS).pollDelay(100, TimeUnit.MILLISECONDS)
                .until(testListener.orderDispatchCounter::get, equalTo(1));
    }

    private void sendMessage(String topic, String key,Object data) throws Exception {
        kafkaTemplate.send(
                MessageBuilder
                        .withPayload(data)
                        .setHeader(KafkaHeaders.KEY, key)
                        .setHeader(KafkaHeaders.TOPIC, topic)
                        .build()).get();
    }
}
