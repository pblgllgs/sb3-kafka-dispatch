package com.pblgllgs.util;
/*
 *
 * @author pblgl
 * Created on 23-11-2023
 *
 */

import com.pblgllgs.message.OrderCreated;

import java.util.UUID;

public class TestEventData {

    public static OrderCreated builderOrderCreatedEvent(UUID orderId, String item) {
        return OrderCreated.builder()
                .orderId(orderId)
                .item(item)
                .build();
    }
}
