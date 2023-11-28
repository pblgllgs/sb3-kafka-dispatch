package com.pblgllgs.message;
/*
 *
 * @author pblgl
 * Created on 24-11-2023
 *
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDispatched {
    private UUID orderId;

    private UUID processedById;

    private String notes;
}
