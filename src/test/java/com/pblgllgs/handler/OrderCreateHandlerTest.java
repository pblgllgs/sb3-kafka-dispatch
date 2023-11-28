package com.pblgllgs.handler;

import com.pblgllgs.message.OrderCreated;
import com.pblgllgs.service.DispatchService;
import com.pblgllgs.util.TestEventData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.*;

class OrderCreateHandlerTest {

    private OrderCreateHandler handler;

    private DispatchService dispatchServiceMock;

    @BeforeEach
    void setUp() {
        dispatchServiceMock = mock(DispatchService.class);
        handler = new OrderCreateHandler(dispatchServiceMock);
    }

    @Test
    void listen_Success() throws Exception {
        String key = randomUUID().toString();
        OrderCreated testEvent = TestEventData.builderOrderCreatedEvent(randomUUID(), randomUUID().toString());
        handler.listen(0,key,testEvent);
        verify(dispatchServiceMock, times(1)).process(key, testEvent);
    }

    @Test
    void listen_ServiceThrowsException() throws Exception {
        String key = randomUUID().toString();
        OrderCreated testEvent = TestEventData.builderOrderCreatedEvent(randomUUID(), randomUUID().toString());
        doThrow(new RuntimeException("Service failure")).when(dispatchServiceMock).process(key, testEvent);
        handler.listen(0,key,testEvent);
        verify(dispatchServiceMock, times(1)).process(key,testEvent);
    }
}