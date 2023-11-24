package com.pblgllgs.message;
/*
 *
 * @author pblgl
 * Created on 23-11-2023
 *
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreated {
    UUID orderId;
    String item;
}
