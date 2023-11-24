package com.pblgllgs.handler;

import com.pblgllgs.message.OrderCreated;
import com.pblgllgs.service.DispatchService;
import com.pblgllgs.util.TestEventData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

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
        OrderCreated testEvent = TestEventData.builderOrderCreatedEvent(UUID.randomUUID(),UUID.randomUUID().toString());
        handler.listen(testEvent);
        verify(dispatchServiceMock, times(1)).process(testEvent);
    }

    @Test
    void listen_ServiceThrowsException() throws Exception {
        OrderCreated testEvent = TestEventData.builderOrderCreatedEvent(UUID.randomUUID(),UUID.randomUUID().toString());
        doThrow(new RuntimeException("Service failure")).when(dispatchServiceMock).process(testEvent);
        handler.listen(testEvent);
        verify(dispatchServiceMock, times(1)).process(testEvent);
    }
}