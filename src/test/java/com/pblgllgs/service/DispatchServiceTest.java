package com.pblgllgs.service;

import com.pblgllgs.message.OrderCreated;
import com.pblgllgs.util.TestEventData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class DispatchServiceTest {

    private DispatchService dispatchService;

    @BeforeEach
    void setUp() {
        dispatchService= new DispatchService();
    }

    @Test
    void process() {
        OrderCreated testEvent = TestEventData.builderOrderCreatedEvent(UUID.randomUUID(),UUID.randomUUID().toString());
        dispatchService.process(testEvent);
    }
}